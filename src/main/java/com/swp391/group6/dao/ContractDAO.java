package com.swp391.group6.dao;

import com.swp391.group6.model.Contract;
import com.swp391.group6.model.User;
import com.swp391.group6.util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContractDAO {

    private static final String BASE_SELECT =
            "SELECT c.*, " +
                    "cu.fullName AS customerName, " +
                    "s.fullName AS staffName, " +
                    "m.fullName AS managerName " +
                    "FROM contracts c " +
                    "JOIN users cu ON c.customerId = cu.id " +
                    "JOIN users s ON c.staffId = s.id " +
                    "LEFT JOIN users m ON c.managerId = m.id " +
                    "WHERE c.isDeleted = 0 ";

    private String resolveSortColumn(String sortBy){
        if("Khach hang".equals(sortBy) || "customer".equals(sortBy)){
            return "cu.fullName";
        }else if("Ngay tao".equals(sortBy) || "createdAt".equals(sortBy)){
            return "c.createdAt";
        }
        return "c.id"; //default
    }

    private String resolveOder(String order){
        return "DESC".equalsIgnoreCase(order) ? "DESC" : "ASC";
    }

    /**
    * thêm điều kiện search vào sql.
    * Search theo: tên khách hàng, tên người tạo, mã hợp dồng, tiểu đề.
    */
    private void appendSreach(StringBuilder sql, List<Object> params,
                              String search){
        if(search == null || search.trim().isEmpty()) return;

        sql.append("AND ( cu.fullName LIKE ? " +
                   "OR s.fullName LIKE ? " +
                   "OR c.contractCode LIKE ? " +
                   "OR c.title LIKE ? )");
        String kw = "%" + search.trim() + "%";
        params.add(kw);
        params.add(kw);
        params.add(kw);
        params.add(kw);
    }

    /**
     * thêm ràng buộc phân quyền vào sql
     * "staff"  -> chỉ xem hợp đồng mình tạo ra (staffId = currentUserId)
     * "customer -> chỉ xem hợp đồng của mình (customerId = currentUserId)
     * null/other -> không giới hạn (MANAGER)
     */
    private void appendOwnerFilter(StringBuilder sql, List<Object> params,
                                  String ownerFilter, int currentUserId){
        if("staff".equals(ownerFilter)){
            sql.append("AND c.staffId = ? ");
            params.add(currentUserId);
        }else  if("customer".equals(ownerFilter)){
            sql.append("AND c.customerId = ? ");
            params.add(currentUserId);
        }
    }

    private void bindParams(PreparedStatement ps, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            Object p = params.get(i);
            if(p instanceof String){
                ps.setString(i + 1, (String) p);
            }else if(p instanceof Integer){
                ps.setInt(i + 1, (Integer) p);
            }
        }
    }

    /**
    *Lấy danh sách hợp đồng có filter + phần mềm
     * * @param sreach         từ khóa tiềm kiếm
     * * @param sortby         sắp xếp theo trường nào
     * * @param order          ASC hoặc DESC
     * * @param creatorId      lọc theo staff (chi MANAGER được dùng
     * *                       tham số này)
     * * @param owerFilter     "staff" / "customer" / null
     * * @param currentUserId  id của user đang đăng nhập
     * * @param page           trang hiển thị
     * * @param pageZise       số động mỗi trang
    */
    // Get contracts with filters and pagination
    public List<Contract> findWithFilters(String search, String sortBy,
                                          String order, Integer creatorId,
                                          String ownerFilter, int currentUserId,
                                          int page, int pageSize){

        List<Contract> contracts = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder(BASE_SELECT);

        //1. Ràng buộc phần quyền (phải dùng trước filter creator của Manager)
        appendOwnerFilter(sql,params,ownerFilter, currentUserId);

        //2. Filter creator(chi MANAGER su dung) neu la STAFF/CUSTOMER bỏ qua
        if(creatorId != null && ownerFilter == null){
            sql.append("AND c.staffId = ? ");
            params.add(creatorId);
        }

        //3. search
        appendSreach(sql,params,search);

        //4. sort
        String sortCol = resolveSortColumn(sortBy);
        String sortOrder = resolveOder(order);
        sql.append("ORDER BY ")
                .append(sortCol).append(" ")
                .append(sortOrder).append(" ");

        //5. Pagination
        sql.append("LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        try(Connection conn = DBContext.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString())){

            bindParams(ps, params);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    contracts.add(mapResultSet(rs));
                }
            }

        }catch (SQLException e){
            System.out.println("ContractDAO.findWithFilters failed: " +
                                e.getMessage());
            e.printStackTrace();
        }

        return contracts;
    }

    /**
     * đem tổng hợp số hộp đồng theo filter + phân quyền
     *
     */
    // Count contracts with filters (for pagination)
    public int countWithFilters(String search, String sortBy,
                                String order, Integer creatorId,
                                String ownerFilter, int currentUserId) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) " +
                        "FROM contracts c " +
                        "JOIN users cu ON c.customerId = cu.id " +
                        "JOIN users s ON c.staffId = s.id " +
                        "WHERE c.isDeleted = 0 "
        );

        appendOwnerFilter(sql,params,ownerFilter, currentUserId);

        if(creatorId != null && ownerFilter == null){
            sql.append("AND c.staffId = ? ");
            params.add(creatorId);
        }

        appendSreach(sql,params,search);

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            bindParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("ContractDAO.coundWithFilters failed: " +
                    e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    // Find contract by ID
    public Contract findById(int id){
        String sql = BASE_SELECT + "AND c.id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    return mapResultSet(rs);
                }
            }

        }catch (SQLException e) {
            System.err.println("ContractDAO.findById failed: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Lấy danh sách user có quyền CONTRACT_CREATE
     * cho dropdown "All Creators" (chỉ MANAGER dùng)
     */
    //Get all staff users for creator filter dropdown
    public List<User> getAllStaff(){
        List<User> list = new ArrayList<>();

        String sql = "SELECT  DISTINCT u.id, u.fullName, u.email " +
                     "FROM users u " +
                     "JOIN roles r ON u.roleId = r.id " +
                     "JOIN role_permissions rp ON r.id = rp.roleId " +
                     "JOIN permissions p ON rp.permissionId = p.id " +
                     "WHERE p.name = 'CONTRACT_CREATE' " +
                     "AND u.status = 1 " +
                     "AND u.isDeleted = 0 " +
                     "ORDER BY u.fullName ASC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while(rs.next()){
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setFullName(rs.getString("fullName"));
                u.setEmail(rs.getString("email"));
                list.add(u);
            }

        }catch (SQLException e){
            System.err.println("ContractDAO.getAllStaff failed: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    // Deactivate a contract (set status = REJECTED)
    public boolean deactivate(int id, int managerId){
        String sql = "UPDATE contracts SET status = 'REJECTED', " +
                "managerId = ?, approvedAt = NOW(), " +
                "updatedAt = NOW() " +
                "WHERE id = ? AND isDeleted = 0 ";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, managerId);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;

        }catch (SQLException e){
            System.err.println("ContractDAO.deactivate failed: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    //Map ResultSet row to Contract object
    private Contract mapResultSet(ResultSet rs) throws SQLException{
        Contract c = new Contract();

        c.setId(rs.getInt("id"));
        c.setContractCode(rs.getString("contractCode"));
        c.setCustomerId(rs.getInt("customerId"));
        c.setStaffId(rs.getInt("staffId"));

        int managerId = rs.getInt("managerId");
        c.setManagerId(rs.wasNull() ? null : managerId);

        c.setTitle(rs.getString("title"));
        c.setTotalAmount(rs.getBigDecimal("totalAmount"));
        c.setSaleDate(rs.getDate("saleDate"));
        c.setStatus(rs.getString("status"));
        c.setRejectionReason(rs.getString("rejectionReason"));
        c.setApprovedAt(rs.getTimestamp("approvedAt"));
        c.setDeleted(rs.getBoolean("isDeleted"));

        Timestamp createdAt = rs.getTimestamp("createdAt");
        c.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);

        Timestamp updatedAt = rs.getTimestamp("updatedAt");
        c.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

        c.setCustomerName(rs.getString("customerName"));
        c.setStaffName(rs.getString("staffName"));
        c.setManagerName(rs.getString("managerName"));

        return c;
    }
}

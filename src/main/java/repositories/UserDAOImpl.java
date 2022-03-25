package repositories;

import models.Reimbursement;
import models.User;
import util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO{

    @Override
    public List<Reimbursement> viewPastReimbs(Integer userId) {
        List<Reimbursement> reimbs = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT r.reimb_id, r.reimb_amount, " +
                    "r.reimb_submitted, r.reimb_resolved, r.reimb_description, " +
                    "u.ers_username, us.ers_username, r.reimb_status_id, r.reimb_type_id " +
                    "FROM ers_reimbursement r " +
                    "LEFT JOIN ers_users u ON r.reimb_author = u.ers_user_id " +
                    "LEFT JOIN ers_users us ON r.reimb_resolver = us.ers_user_id " +
                    "WHERE r.reimb_author = ?" +
                    "ORDER BY r.reimb_status_id;";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                reimbs.add (new Reimbursement(
                        rs.getInt(1),
                        rs.getDouble(2),
                        rs.getDate(3),
                        rs.getDate(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9)
                ));
            }

        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        return reimbs;
    }

    @Override
    public List<Reimbursement> viewAllReimbs() {
        List<Reimbursement> reimbs = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT r.reimb_id, r.reimb_amount, " +
                    "r.reimb_submitted, r.reimb_resolved, r.reimb_description, " +
                    "u.ers_username, us.ers_username, r.reimb_status_id, r.reimb_type_id " +
                    "FROM ers_reimbursement r " +
                    "LEFT JOIN ers_users u ON r.reimb_author = u.ers_user_id " +
                    "LEFT JOIN ers_users us ON r.reimb_resolver = us.ers_user_id " +
                    "ORDER BY r.reimb_submitted;";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                reimbs.add (new Reimbursement(
                        rs.getInt(1),
                        rs.getDouble(2),
                        rs.getDate(3),
                        rs.getDate(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9)
                ));
            }

        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        return reimbs;
    }

    @Override
    public List<Reimbursement> filterReimbsByStatus(Integer statusId) {
        List<Reimbursement> reimbs = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT r.reimb_id, r.reimb_amount, " +
                    "r.reimb_submitted, r.reimb_resolved, r.reimb_description, " +
                    "u.ers_username, us.ers_username, r.reimb_status_id, r.reimb_type_id " +
                    "FROM ers_reimbursement r " +
                    "LEFT JOIN ers_users u ON r.reimb_author = u.ers_user_id " +
                    "LEFT JOIN ers_users us ON r.reimb_resolver = us.ers_user_id " +
                    "WHERE r.reimb_status_id = ?" +
                    "ORDER BY r.reimb_submitted;";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, statusId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                reimbs.add (new Reimbursement(
                        rs.getInt(1),
                        rs.getDouble(2),
                        rs.getDate(3),
                        rs.getDate(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9)
                ));
            }

        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        return reimbs;
    }

    @Override
    public void approveReimb(Integer reimbId, Integer userId) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "UPDATE ers_reimbursement SET reimb_status_id = 2, reimb_resolved = now(), reimb_resolver = ? WHERE reimb_id = ?;";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, reimbId);

            ps.executeUpdate();

        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
    }

    @Override
    public void denyReimb(Integer reimbId, Integer userId) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "UPDATE ers_reimbursement SET reimb_status_id = 3, reimb_resolved = now(), reimb_resolver = ? WHERE reimb_id = ?;";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, reimbId);

            ps.executeUpdate();

        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
    }

    @Override
    public void addReimb(Integer userId, Reimbursement reimbursement) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "INSERT INTO ers_reimbursement (reimb_amount, reimb_submitted, reimb_description, reimb_author, reimb_type_id) VALUES (?, DEFAULT, ?, ?, ?); ";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, reimbursement.getAmount());
            ps.setString(2, reimbursement.getDescription());
            ps.setInt(3, userId);
            ps.setInt(4, Integer.parseInt(reimbursement.getType()));

            ps.executeUpdate();

        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
    }

    @Override
    public User getUser(String username) {
        User user = null;

        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM ers_users WHERE ers_username = ?;";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                user = new User(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getInt(7)
                );
            }

        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        return user;
    }

    @Override
    public User getUserById(Integer userId) {
        User user = null;

        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM ers_users WHERE ers_user_id = ?;";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                user = new User(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getInt(7)
                );
            }

        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        return user;
    }

    @Override
    public Reimbursement getReimb(Integer reimbId) {
        Reimbursement reimb = null;

        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM ers_reimbursement WHERE reimb_id = ?;";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, reimbId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                reimb = new Reimbursement(
                        rs.getInt(1),
                        rs.getDouble(2),
                        rs.getDate(3),
                        rs.getDate(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9)
                );
            }

        } catch(SQLException sqle){
            sqle.printStackTrace();
        }

        return reimb;
    }
}

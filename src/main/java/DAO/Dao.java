package DAO;

import java.sql.*;
import java.util.Map;

public class Dao {

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rset = null;

    public int insertData(Map<String, String> paramMap){

        int result = 0;
        con = getConnection(con);
        String query = "INSERT INTO keyword(word, reg_date, write_date, locx, locy) VALUES (?, now()::timestamp(0), to_timestamp(?, 'yyyy-mm-dd hh24:mi:ss'), ?, ?)";

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, paramMap.get("wordList"));
            pstmt.setString(2, paramMap.get("writeDate"));
            pstmt.setString(3, paramMap.get("locX"));
            pstmt.setString(4, paramMap.get("locY"));

            if(pstmt.executeUpdate() > 0){
                query = "SELECT MAX(idx_seq) AS idx_seq FROM keyword";
                pstmt = con.prepareStatement(query);
                rset = pstmt.executeQuery();
                if(rset.next()){
                    result = rset.getInt("idx_seq");
                }
            };

            /*if(result > 0){
                con.commit();
            }*/
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            close(rset);
            close(pstmt);
            close(con);
        }

        return result;
    }

    public int keywordCnt(Map<String, String> paramMap) {
        int result = 0;
        con = getConnection(con);
        String query = "INSERT INTO keyword_cnt(idx_seq, word) VALUES (?, ?)";

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.valueOf(paramMap.get("idxSeq")));
            pstmt.setString(2, paramMap.get("word"));
            result = pstmt.executeUpdate();


            /*if(result > 0){
                con.commit();
            }*/
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            close(pstmt);
            close(con);
        }

        return result;
    }

    /***
     *
     * ***/
    public static Connection getConnection(Connection con){
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/first",
                    "postgres",
                    "root"
            );
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return con;
    }

    public static void close(Connection con){
        try {
            if(!con.isClosed() && con!=null){
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(Statement stmt){
        try {
            if(!stmt.isClosed() && stmt!=null){
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(PreparedStatement pstmt){
        try {
            if(!pstmt.isClosed() && pstmt!=null){
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(ResultSet rset){
        try {
            if(!rset.isClosed() && rset!=null){
                rset.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

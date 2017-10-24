/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Raakaaine;

public class RaakaaineDao implements Dao<Raakaaine, Integer> {

    private Database database;

    public RaakaaineDao(Database database) {
        this.database = database;
    }

    @Override
    public Raakaaine findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        Raakaaine o = new Raakaaine(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }

    @Override
    public List<Raakaaine> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine ORDER BY nimi");

        ResultSet rs = stmt.executeQuery();
        List<Raakaaine> aineet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            aineet.add(new Raakaaine(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return aineet;
    }
    
    public List<Raakaaine> findAllWithAnnos(Integer key) throws SQLException {
        
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT RaakaAine.id,"
                + "RaakaAine.nimi FROM RaakaAine, Annos, AnnosRaakaAine WHERE"
                + " Annos.id = ? AND Annos.id = AnnosRaakaAine.annos_id AND"
                + " AnnosRaakaAine.raakaaine_id = RaakaAine.id");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        List<Raakaaine> aineet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            aineet.add(new Raakaaine(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return aineet;
    }
        

    @Override
    public void delete(Integer key) throws SQLException {
        
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM RaakaAine WHERE id = ?");
        
        stmt.setObject(1, key);
        stmt.executeUpdate();
        
        stmt.close();
        connection.close();
    }
    
    @Override
    public Raakaaine saveOrUpdate(Raakaaine aine) throws SQLException {
        if (aine.getNimi() == null || aine.getNimi().length() <= 0) {
            throw new java.lang.RuntimeException("Ei voi luoda raaka-ainetta jolla ei ole nimeä!");
        }
        
        Raakaaine haettu = findByName(aine.getNimi());
        
        if (haettu != null) {
            return haettu;
        }
        
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO RaakaAine"
                + " (nimi)"
                + " VALUES (?)");
        stmt.setString(1, aine.getNimi());
        stmt.executeUpdate();
          
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                Raakaaine a = new Raakaaine(rs.getInt(1), aine.getNimi());
                stmt.close(); 
                conn.close();
                return a;
            }
            else {
                throw new SQLException("Ei saatu avainta!");
            }
        }
        
    }
    

    private Raakaaine findByName(String name) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Raakaaine WHERE nimi = ?");
            stmt.setString(1, name);

            try (ResultSet result = stmt.executeQuery()) {
                if (!result.next()) {
                    return null;
                }

                return new Raakaaine(0, "");
            }
        }
    }
}
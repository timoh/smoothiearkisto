package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.AnnosRaakaaine;
import tikape.runko.domain.Raakaaine;

public class AnnosRaakaaineDao implements Dao<AnnosRaakaaine, Integer> {

    private Database database;

    public AnnosRaakaaineDao(Database database) {
        this.database = database;
    }

    @Override
    public AnnosRaakaaine findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String ohje = rs.getString("ohje");
        Integer annos_id = rs.getInt("annos_id");
        Integer raakaaine_id = rs.getInt("raakaaine_id");
        Integer jarjestys = rs.getInt("jarjestys");
        String maara = rs.getString("maara");

        AnnosRaakaaine o = new AnnosRaakaaine(id, ohje, jarjestys, annos_id, raakaaine_id, maara);

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }
    
    // Tässä toteutuksessa metodia ei tarvita
    public List<AnnosRaakaaine> findAll() throws SQLException {
        return null;
    }
    

    // Koska käytännössä halutaan aina hakea ohjeet tietylle annokselle, implementoidaan
    // metodi, jolla haetaan yhtä annosta vastaavat työvaiheet
    public List<AnnosRaakaaine> findAllWithAnnos(Integer key) throws SQLException {
    
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine WHERE annos_id = ?"
                + " ORDER BY jarjestys");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        List<AnnosRaakaaine> ohjeet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String ohje = rs.getString("ohje");
            Integer annos_id = rs.getInt("annos_id");
            Integer raakaaine_id = rs.getInt("raakaaine_id");
            Integer jarjestys = rs.getInt("jarjestys");
            String maara = rs.getString("maara");

            ohjeet.add(new AnnosRaakaaine(id, ohje, jarjestys, annos_id, raakaaine_id, maara));
        }

        rs.close();
        stmt.close();
        connection.close();

        return ohjeet;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM AnnosRaakaAine WHERE id = ?");
        stmt.setObject(1, key);
        stmt.executeQuery();
        stmt.close();
        connection.close();
    }
    
    @Override
    public AnnosRaakaaine saveOrUpdate(AnnosRaakaaine ohje) throws SQLException {
        if (ohje.getJarjestys() == null || ohje.getJarjestys() <= 0) {
            throw new java.lang.RuntimeException("Työvaiheen järjestysluku oltava positiivinen");
        }
        
        if (ohje.getId() == null) {
            return save(ohje);
        } else {
            return update(ohje);
        }
    }
    
    public boolean isRaakaaineUsed(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) "
                + "AS maara FROM AnnosRaakaAine WHERE raakaaine_id = ?");
        stmt.setObject(1, key);
        
        ResultSet rs = stmt.executeQuery();
        
        if (rs.getInt("maara") == 0) {
            stmt.close(); 
            connection.close();
            return false;
        }
        
        stmt.close(); 
        connection.close();
        return true;
        
        
    }
    
    private AnnosRaakaaine save(AnnosRaakaaine ohje) throws SQLException {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO AnnosRaakaAine"
                + " (annos_id, raakaaine_id, jarjestys, maara, ohje)"
                + " VALUES (?, ?, ?, ?, ?)");
        stmt.setInt(1, ohje.getAnnos_id());
        stmt.setInt(2, ohje.getRaakaaine_id());
        stmt.setInt(3, ohje.getJarjestys());
        stmt.setString(4, ohje.getMaara());
        stmt.setString(5, ohje.getOhje());

        stmt.executeUpdate();
          
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                AnnosRaakaaine a = new AnnosRaakaaine(rs.getInt(1), ohje.getOhje(),
                    ohje.getJarjestys(), ohje.getAnnos_id(), ohje.getRaakaaine_id(),
                    ohje.getMaara());
                stmt.close(); 
                conn.close();
                return a;
            }
            else {
                throw new SQLException("Ei saatu avainta!");
            }
        }

    }

    private AnnosRaakaaine update(AnnosRaakaaine ohje) throws SQLException {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE AnnosRaakaAine SET"
                + " ohje = ? WHERE id = ?");
        stmt.setString(1, ohje.getOhje());
        stmt.setInt(5, ohje.getId());

        stmt.executeUpdate();

        stmt.close();
        conn.close();

        return ohje;
    }
        
}
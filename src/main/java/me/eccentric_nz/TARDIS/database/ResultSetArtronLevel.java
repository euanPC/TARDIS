package me.eccentric_nz.TARDIS.database;

import me.eccentric_nz.TARDIS.TARDIS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetArtronLevel {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private int artronLevel;
    private final String uuid;
    private final String prefix;

    public ResultSetArtronLevel(TARDIS plugin, String uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        prefix = this.plugin.getPrefix();
    }

    public boolean resultset() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT artron_level FROM " + prefix + "tardis WHERE uuid = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                artronLevel = rs.getInt("artron_level");
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for tardis (artron level) table! " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing tardis (artron level) statement! " + e.getMessage());
            }
        }
        return true;
    }

    public int getArtronLevel() {
        return artronLevel;
    }
}

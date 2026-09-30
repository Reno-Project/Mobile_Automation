package com.mobile.automation.db;

import com.mobile.automation.config.CapabilitiesConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Fetches the latest OTP from the Reno test SQL Server database.
 */
public final class DbHelper {

    private static final String OTP_QUERY = "SELECT TOP 1 otp FROM otp ORDER BY id DESC";

    private DbHelper() {
    }

    /**
     * @return latest OTP string
     * @throws IllegalStateException if connection/query fails or OTP is blank
     */
    public static String fetchLatestOtp() {
        String url = CapabilitiesConfig.getDbUrl();
        String username = CapabilitiesConfig.getDbUsername();
        String password = CapabilitiesConfig.getDbPassword();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(OTP_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            if (!resultSet.next()) {
                System.out.println("OTP is incorrect or missing — no rows returned from otp table");
                throw new IllegalStateException("No OTP found in database");
            }

            String otp = resultSet.getString("otp");
            if (otp == null || otp.isBlank()) {
                System.out.println("OTP is incorrect or missing — blank OTP value");
                throw new IllegalStateException("OTP value is blank");
            }

            System.out.println("OTP from DB: " + otp.trim());
            return otp.trim();
        } catch (SQLException e) {
            System.out.println("OTP is incorrect — database error: " + e.getMessage());
            throw new IllegalStateException("Failed to fetch OTP from database", e);
        }
    }
}

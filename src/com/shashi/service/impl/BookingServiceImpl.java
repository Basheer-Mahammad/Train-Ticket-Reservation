package com.shashi.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.shashi.beans.HistoryBean;
import com.shashi.beans.TrainException;
import com.shashi.constant.ResponseCode;
import com.shashi.service.BookingService;
import com.shashi.utility.DBUtil;

//Service Implementaion class for booking details of the ticket
//Creates the booking history and save to database
public class BookingServiceImpl implements BookingService {

	@Override
	public List<HistoryBean> getAllBookingsByCustomerId(String customerEmailId) throws TrainException {
		List<HistoryBean> transactions = null;
	   String query = "SELECT * FROM HISTORY WHERE user_id=?";
		try {
		   Connection con = DBUtil.getConnection();
		   PreparedStatement ps = con.prepareStatement(query);
		   ps.setString(1, customerEmailId); // This should be the user_id (int), but the method receives email. We'll need to convert email to user_id.
		   ResultSet rs = ps.executeQuery();
		   transactions = new ArrayList<HistoryBean>();
		   while (rs.next()) {
			   HistoryBean transaction = new HistoryBean();
			   transaction.setTransId(String.valueOf(rs.getInt("id")));
			   // The following fields are not present in the new schema, so set as empty or fetch if available
			   transaction.setFrom_stn("");
			   transaction.setTo_stn("");
			   transaction.setDate(rs.getString("journey_date"));
			   transaction.setMailId("");
			   transaction.setSeats(rs.getInt("seats"));
			   transaction.setAmount(0.0); // No amount column in new schema
			   transaction.setTr_no(String.valueOf(rs.getLong("train_no")));
			   transactions.add(transaction);
		   }

			ps.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new TrainException(e.getMessage());
		}
		return transactions;
	}

	@Override
	public HistoryBean createHistory(HistoryBean details) throws TrainException {
		HistoryBean history = null;
		String query = "INSERT INTO HISTORY VALUES(?,?,?,?,?,?,?,?)";
		try {
			Connection con = DBUtil.getConnection();
			PreparedStatement ps = con.prepareStatement(query);
			String transactionId = UUID.randomUUID().toString();
			ps.setString(1, transactionId);
			ps.setString(2, details.getMailId());
			ps.setString(3, details.getTr_no());
			ps.setString(4, details.getDate());
			ps.setString(5, details.getFrom_stn());
			ps.setString(6, details.getTo_stn());
			ps.setLong(7, details.getSeats());
			ps.setDouble(8, details.getAmount());
			int response = ps.executeUpdate();
			if (response > 0) {
				history = (HistoryBean) details;
				history.setTransId(transactionId);
			} else {
				throw new TrainException(ResponseCode.INTERNAL_SERVER_ERROR);
			}
			ps.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new TrainException(e.getMessage());
		}
		return history;
	}

}

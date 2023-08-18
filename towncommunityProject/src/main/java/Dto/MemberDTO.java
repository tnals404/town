package Dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class MemberDTO {
	String member_id,name,phone,email,password;
	int stop_date,report_count,town_id,point;
	String address,profile_image,nowpassword;
	Date signup_date,stopclear_date,delete_date;
	String signup_date_str,stopclear_date_str;
	int invite_sum,member_role;
	String grade_name;
	TownDTO town;
	
	
	public int getMember_role() {
		return member_role;
	}
	public void setMember_role(int member_role) {
		this.member_role = member_role;
	}
	public String getNowpassword() {
		return nowpassword;
	}
	public void setNowpassword(String nowpassword) {
		this.nowpassword = nowpassword;
	}
	public Date getStopclear_date() {
		return stopclear_date;
	}
	public void setStopclear_date(Date stopclear_date) {
		this.stopclear_date = stopclear_date;
	}
	public Date getDelete_date() {
		return delete_date;
	}
	public void setDelete_date(Date delete_date) {
		this.delete_date = delete_date;
	}
	public Date getSignup_date() {
		return signup_date;
	}
	public void setSignup_date(Date signup_date) {
		this.signup_date = signup_date;
	}
	public int getInvite_sum() {
		return invite_sum;
	}
	public void setInvite_sum(int invite_sum) {
		this.invite_sum = invite_sum;
	}
	public TownDTO getTown() {
		return town;
	}
	public void setTown(TownDTO town) {
		this.town = town;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getStop_date() {
		return stop_date;
	}
	public void setStop_date(int stop_date) {
		this.stop_date = stop_date;
	}
	public int getReport_count() {
		return report_count;
	}
	public void setReport_count(int report_count) {
		this.report_count = report_count;
	}
	public int getTown_id() {
		return town_id;
	}
	public void setTown_id(int town_id) {
		this.town_id = town_id;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProfile_image() {
		return profile_image;
	}
	public void setProfile_image(String profile_image) {
		this.profile_image = profile_image;
	}
	public String getGrade_name() {
		return grade_name;
	}
	public void setGrade_name(String grade_name) {
		this.grade_name = grade_name;
	}
	public String getStopclear_date_str() {
		return stopclear_date_str;
	}
	public void setStopclear_date_str(String stopclear_date_str) {
		this.stopclear_date_str = stopclear_date_str;
	}
	public String getSignup_date_str() {
		return signup_date_str;
	}
	public void setSignup_date_str(String signup_date_str) {
		this.signup_date_str = signup_date_str;
	}
	
}

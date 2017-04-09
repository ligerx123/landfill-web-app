package org.lacitysan.landfill.server.persistence.enums;

import org.lacitysan.landfill.server.json.LandfillEnumDeserializer;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * This file was automatically generated by 'user-permission-enum-gen'.
 * https://github.com/landfill-eforms/landfill-tools/tree/master/user-permission-enum-gen
 *
 * Data was parsed from the Excel sheet located at https://docs.google.com/spreadsheets/d/1qD6SvrIjW-FTFVWyO6cMKr4DaV_laGqgzV4Im_wNMWc
 */
public enum UserPermission {

	/** Admin */
	ADMIN ("Admin", "Admin"),

	/** User can view existing users. */
	VIEW_USERS ("View Users", Category.USERS, "View List", "User can view existing users."),

	/** User can create new users. */
	CREATE_USERS ("Create Users", Category.USERS, "Create", "User can create new users."),

	/** User can reactivate other non-admin user accounts. */
	ENABLE_USERS ("Enable Users", Category.USERS, "Enable Account", "User can reactivate other non-admin user accounts."),

	/** User can deactivate other non-admin user accounts. */
	DISABLE_USERS ("Disable Users", Category.USERS, "Disable Account", "User can deactivate other non-admin user accounts."),

	/** User can reset the passwords of other non-admin users. */
	RESET_USER_PASSWORDS ("Reset User Passwords", Category.USERS, "Reset Password", "User can reset the passwords of other non-admin users."),

	/** User can reset the usernames of other non-admin users. */
	RESET_USER_USERNAMES ("Reset User Usernames", Category.USERS, "Reset Username", "User can reset the usernames of other non-admin users."),

	/** User can edit the profiles of other non-admin users. */
	EDIT_USER_PROFILES ("Edit User Profiles", Category.USERS, "Edit Profile", "User can edit the profiles of other non-admin users."),

	/** User can change the employee ID associated with non-admin user accounts. */
	ASSIGN_EMPLOYEE_ID ("Assign Employee Id", Category.USERS, "Assign Employee ID", "User can change the employee ID associated with non-admin user accounts."),

	/** User can assign user groups to non-admin users. */
	ASSIGN_USER_GROUPS ("Assign User Groups", Category.USERS, "Assign Groups", "User can assign user groups to non-admin users."),

	/** User can view existing user groups. */
	VIEW_USER_GROUPS ("View User Groups", Category.USER_GROUPS, "View List", "User can view existing user groups."),

	/** User can create new user groups. */
	CREATE_USER_GROUPS ("Create User Groups", Category.USER_GROUPS, "Create", "User can create new user groups."),

	/** User can delete existing user groups. */
	DELETE_USER_GROUPS ("Delete User Groups", Category.USER_GROUPS, "Delete", "User can delete existing user groups."),

	/** User can add/remove roles and rename existing user groups. */
	EDIT_USER_GROUPS ("Edit User Groups", Category.USER_GROUPS, "Edit", "User can add/remove roles and rename existing user groups."),

	/** User can view existing equipment. */
	VIEW_INSTRUMENTS ("View Instruments", Category.EQUIPMENT, "View List", "User can view existing equipment."),

	/** User can add new equipment entries. */
	CREATE_INSTRUMENTS ("Create Instruments", Category.EQUIPMENT, "Create", "User can add new equipment entries."),

	/** User can edit existing equipment entries. */
	EDIT_INSTRUMENTS ("Edit Instruments", Category.EQUIPMENT, "Edit", "User can edit existing equipment entries."),

	/** User can delete existing equipment entries. */
	DELETE_INSTRUMENTS ("Delete Instruments", Category.EQUIPMENT, "Delete", "User can delete existing equipment entries."),

	/** User can view existing equipment types. */
	VIEW_INSTRUMENT_TYPES ("View Instrument Types", Category.EQUIPMENT_TYPE, "View List", "User can view existing equipment types."),

	/** User can add new equipment types. */
	CREATE_INSTRUMENT_TYPES ("Create Instrument Types", Category.EQUIPMENT_TYPE, "Create", "User can add new equipment types."),

	/** User can edit existing equipment types. */
	EDIT_INSTRUMENT_TYPES ("Edit Instrument Types", Category.EQUIPMENT_TYPE, "Edit", "User can edit existing equipment types."),

	/** User can delete existing equipment types. */
	DELETE_INSTRUMENT_TYPES ("Delete Instrument Types", Category.EQUIPMENT_TYPE, "Delete", "User can delete existing equipment types."),

	/** User can upload data form the mobile application. */
	UPLOAD_MOBILE_DATA ("Upload Mobile Data", Category.MOBILE_APP_SYNC, "Upload", "User can upload data form the mobile application."),

	/** User can download data for transfer to the mobile applications. */
	DOWNLOAD_MOBILE_DATA ("Download Mobile Data", Category.MOBILE_APP_SYNC, "Download", "User can download data for transfer to the mobile applications."),

	/** User can view existing unverified data sets. */
	VIEW_UNVERIFIED_DATA_SETS ("View Unverified Data Sets", Category.UNVERIFIED_DATA, "View List", "User can view existing unverified data sets."),

	/** User can view the details of an unverified data set. <i>Subject to change</i> */
	VIEW_UNVERIFIED_DATA_SET ("View Unverified Data Set", Category.UNVERIFIED_DATA, "View Sets", "User can view the details of an unverified data set."),

	/** User can edit and save the details of an unverified data set. <i>Subject to change</i> */
	EDIT_UNVERIFIED_DATA_SET ("Edit Unverified Data Set", Category.UNVERIFIED_DATA, "Edit Sets", "User can edit and save the details of an unverified data set."),

	/** User can delete entire unverified data sets. <i>Subject to change</i> */
	DELETE_UNVERIFIED_DATA_SET ("Delete Unverified Data Set", Category.UNVERIFIED_DATA, "Delete Sets", "User can delete entire unverified data sets."),

	/** User can delete individual data entries in an unverified data set. <i>Subject to change</i> */
	DELETE_UNVERIFIED_DATA ("Delete Unverified Data", Category.UNVERIFIED_DATA, "Delete Data", "User can delete individual data entries in an unverified data set."),

	/** User can commit unverified data sets. <i>Subject to change</i> */
	COMMIT_UNVERIFIED_DATA_SET ("Commit Unverified Data Set", Category.UNVERIFIED_DATA, "Commit Set", "User can commit unverified data sets."),

	/** User can generate reports. <i>Do we need separate permissions for each type of reports?</i> */
	GENERATE_REPORTS ("Generate Reports", Category.REPORTS, "Generate", "User can generate reports."),

	/** User can schedule automated email reports. */
	SCHEDULE_EMAIL_REPORTS ("Schedule Email Reports", Category.SCHEDULE, "Reports", "User can schedule automated email reports."),

	/** User can schedule automated email notifcations. */
	SCHEDULE_EMAIL_NOTIFICATIONS ("Schedule Email Notifications", Category.SCHEDULE, "Notifications", "User can schedule automated email notifcations.");

	private String name;
	private String category;
	private String categoryAction;
	private String description;

	private UserPermission(String name, String description) {
		this.name = name;
		this.description = description;
	}

	private UserPermission(String name, String category, String categoryAction, String description) {
		this.name = name;
		this.category = category;
		this.categoryAction = categoryAction;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public String getCategoryAction() {
		return categoryAction;
	}

	public String getDescription() {
		return description;
	}

	@JsonCreator
	public static UserPermission deserialize(Object object) {
		return LandfillEnumDeserializer.deserialize(UserPermission.class, object);
	}

	private static class Category {
		protected final static String EQUIPMENT = "Equipment";
		protected final static String EQUIPMENT_TYPE = "Equipment Type";
		protected final static String MOBILE_APP_SYNC = "Mobile App Sync";
		protected final static String REPORTS = "Reports";
		protected final static String SCHEDULE = "Schedule";
		protected final static String UNVERIFIED_DATA = "Unverified Data";
		protected final static String USERS = "Users";
		protected final static String USER_GROUPS = "User Groups";
	}

}
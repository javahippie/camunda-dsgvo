package de.ilume.camunda.showcase.constants;

public class ProcessConstants {

    public static final String PROCESS_DEFINITION_KEY = "DSGVO_Process";

    public static final String CANDIDATE_GROUP_ACCOUNTING = "accounting";

    public static final String VARIABLE_IS_DSGVO_REQUEST = "isDsgvoRequest";
    public static final String VARIABLE_APPROVAL_GIVEN = "approvalGiven";
    public static final String VARIABLE_SEARCH_IN_HR = "searchInHr";
    public static final String VARIABLE_SEARCH_IN_ACCOUNTING = "searchInAccounting";
    public static final String VARIABLE_SEARCH_IN_SALES = "searchInSales";
    public static final String VARIABLE_ACCOUNTING_REPORT_DATA = "accountingData";
    public static final String VARIABLE_SALES_REPORT_DATA = "salesData";
    public static final String VARIABLE_HR_REPORT_DATA = "hrData";

    public static final String VARIABLE_FIRST_NAME = "firstName";
    public static final String VARIABLE_LAST_NAME = "lastName";
    public static final String VARIABLE_STREET = "street";
    public static final String VARIABLE_ZIP_CODE = "zipCode";
    public static final String VARIABLE_CITY = "city";
    public static final String VARIABLE_PHONE_NUMBER = "phoneNumber";
    public static final String VARIABLE_EMAIL ="eMail";
    public static final String VARIABLE_REQUEST_TEXT ="requestText";

    public static final String VARIABLE_IDENTITY_CONFIRMED = "identityConfirmed";

    public static final String BOUNDARY_EVENT_FIVE_DAYS_BEFORE = "BoundaryEvent_FiveDaysBeforeDeadline";
    public static final String BOUNDARY_EVENT_AT_DEADLINE ="BoundaryEvent_AtDeadline";

    public static final String END_EVENT_FIVE_DAY_NOTIFICATION = "EndEvent_FiveDayNotificationSent";
    public static final String END_EVENT_FINAL_NOTIFICATION = "EndEvent_FinalNotificationSent";
}



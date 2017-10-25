/*
 *
 *  Copyright (c) Microsoft. All rights reserved.
 *  Licensed under the MIT license. See LICENSE file in the project root for full license information.
 *
 */

package com.microsoft.azure.sdk.iot.provisioning.device.internal.parser;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class OperationRegistrationStatusParser
{
    private static final String REGISTRATION_ID = "registrationId";
    @SerializedName(REGISTRATION_ID)
    private String registrationId;

    private static final String REGISTRATION_DATE_TIME_UTC = "registrationDateTimeUtc";
    @SerializedName(REGISTRATION_DATE_TIME_UTC)
    private String registrationDateTimeUtc;
    private Date registrationDateTimeUtcDate;

    private static final String ASSIGNED_HUB = "assignedHub";
    @SerializedName(ASSIGNED_HUB)
    private String assignedHub;

    private static final String DEVICE_ID = "deviceId";
    @SerializedName(DEVICE_ID)
    private String deviceId;

    private static final String STATUS = "status";
    @SerializedName(STATUS)
    private String status;

    private static final String GENERATION_ID = "generationId";
    @SerializedName(GENERATION_ID)
    private String generationId;

    private static final String LAST_UPDATES_DATE_TIME_UTC = "lastUpdatedDateTimeUtc";
    @SerializedName(LAST_UPDATES_DATE_TIME_UTC)
    private String lastUpdatesDateTimeUtc;
    private Date lastUpdatesDateTimeUtcDate;

    private static final String ERROR_CODE = "errorCode";
    @SerializedName(ERROR_CODE)
    private String errorCode;

    private static final String ERROR_MESSAGE = "errorMessage";
    @SerializedName(ERROR_MESSAGE)
    private String errorMessage;

    private static final String TPM = "tpm";
    @SerializedName(TPM)
    private OperationsRegistrationStatusTPMParser tpm;

    private static final String X509 = "x509";
    @SerializedName(X509)
    private OperationsRegistrationStatusX509Parser x509;

    public String getRegistrationId()
    {
        return registrationId;
    }

    public String getRegistrationDateTimeUtc()
    {
        return registrationDateTimeUtc;
    }

    public Date getRegistrationDateTimeUtcDate()
    {
        return registrationDateTimeUtcDate;
    }

    public String getAssignedHub()
    {
        return assignedHub;
    }

    public String getDeviceId()
    {
        return deviceId;
    }

    public String getStatus()
    {
        return status;
    }

    public String getGenerationId()
    {
        return generationId;
    }

    public String getLastUpdatesDateTimeUtc()
    {
        return lastUpdatesDateTimeUtc;
    }

    public Date getLastUpdatesDateTimeUtcDate()
    {
        return lastUpdatesDateTimeUtcDate;
    }

    public OperationsRegistrationStatusTPMParser getTpm()
    {
        return tpm;
    }

    public OperationsRegistrationStatusX509Parser getX509()
    {
        return x509;
    }

    public String getErrorCode()
    {
        return errorCode;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }
}

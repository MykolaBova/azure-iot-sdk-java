/*
 *
 *  Copyright (c) Microsoft. All rights reserved.
 *  Licensed under the MIT license. See LICENSE file in the project root for full license information.
 *
 */

package tests.unit.com.microsoft.azure.sdk.iot.dps.device.internal.provisioningtask;

import com.microsoft.azure.sdk.iot.dps.security.SecurityClient;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.contract.ProvisioningDeviceClientContract;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.contract.ResponseCallback;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.contract.UrlPathBuilder;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.exceptions.ProvisioningDeviceClientException;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.exceptions.ProvisioningDeviceSecurityException;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.parser.RegisterRequestParser;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.parser.ResponseParser;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.provisioningtask.Authorization;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.provisioningtask.ResponseData;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.provisioningtask.StatusTask;
import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.net.ssl.SSLContext;

import java.io.IOException;

import static com.microsoft.azure.sdk.iot.provisioning.device.internal.provisioningtask.ContractState.DPS_REGISTRATION_RECEIVED;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/*
    Unit tests for Status task
    Coverage : 80% Method, 82% Line
 */
@RunWith(JMockit.class)
public class StatusTaskTest
{
    private static final String TEST_OPERATION_ID = "testOperationId";
    private static final String TEST_REGISTRATION_ID = "testRegistrationId";

    @Mocked
    SecurityClient mockedSecurityClient;
    @Mocked
    ProvisioningDeviceClientContract mockedProvisioningDeviceClientContract;
    @Mocked
    Authorization mockedAuthorization;
    @Mocked
    RegisterRequestParser mockedRegisterRequestParser;
    @Mocked
    UrlPathBuilder mockedUrlPathBuilder;
    @Mocked
    SSLContext mockedSslContext;
    @Mocked
    ResponseParser mockedResponseParser;
    @Mocked
    ResponseCallback mockedResponseCallback;
    @Mocked
    ResponseData mockedResponseData;

    //SRS_StatusTask_25_001: [ Constructor shall save operationId , dpsSecurityClient, provisioningDeviceClientContract and authorization. ]
    @Test
    public void constructorSucceeds() throws ProvisioningDeviceClientException
    {
        //arrange
        //act
        StatusTask statusTask = Deencapsulation.newInstance(StatusTask.class, new Class[] {SecurityClient.class,
                                                                    ProvisioningDeviceClientContract.class, String.class,
                                                                    Authorization.class},
                                                            mockedSecurityClient, mockedProvisioningDeviceClientContract,
                                                            TEST_OPERATION_ID, mockedAuthorization);
        //assert
        assertNotNull(Deencapsulation.getField(statusTask, "securityClient"));
        assertNotNull(Deencapsulation.getField(statusTask, "provisioningDeviceClientContract"));
        assertNotNull(Deencapsulation.getField(statusTask, "authorization"));
        assertEquals(TEST_OPERATION_ID, Deencapsulation.getField(statusTask, "operationId"));
    }

    //SRS_StatusTask_25_002: [ Constructor shall throw ProvisioningDeviceClientException if operationId , dpsSecurityClient, authorization or provisioningDeviceClientContract is null. ]
    @Test (expected = ProvisioningDeviceClientException.class)
    public void constructorThrowsOnNullSecurityClient() throws ProvisioningDeviceClientException
    {
        //act
        StatusTask statusTask = Deencapsulation.newInstance(StatusTask.class, new Class[] {SecurityClient.class,
                                                                    ProvisioningDeviceClientContract.class, String.class,
                                                                    Authorization.class},
                                                            null, mockedProvisioningDeviceClientContract,
                                                            TEST_OPERATION_ID, mockedAuthorization);
    }

    @Test (expected = ProvisioningDeviceClientException.class)
    public void constructorThrowsOnNullContract() throws ProvisioningDeviceClientException
    {
        //act
        StatusTask statusTask = Deencapsulation.newInstance(StatusTask.class, new Class[] {SecurityClient.class,
                                                                    ProvisioningDeviceClientContract.class, String.class,
                                                                    Authorization.class},
                                                            mockedSecurityClient, null,
                                                            TEST_OPERATION_ID, mockedAuthorization);
    }

    @Test (expected = ProvisioningDeviceClientException.class)
    public void constructorThrowsOnNullOperationId() throws ProvisioningDeviceClientException
    {
        //act
        StatusTask statusTask = Deencapsulation.newInstance(StatusTask.class, new Class[] {SecurityClient.class,
                                                                    ProvisioningDeviceClientContract.class, String.class,
                                                                    Authorization.class},
                                                            mockedSecurityClient, mockedProvisioningDeviceClientContract,
                                                            null, mockedAuthorization);
    }

    @Test (expected = ProvisioningDeviceClientException.class)
    public void constructorThrowsOnEmptyOperationId() throws ProvisioningDeviceClientException
    {
        //act
        StatusTask statusTask = Deencapsulation.newInstance(StatusTask.class, new Class[] {SecurityClient.class,
                                                                    ProvisioningDeviceClientContract.class, String.class,
                                                                    Authorization.class},
                                                            mockedSecurityClient, mockedProvisioningDeviceClientContract,
                                                            "", mockedAuthorization);
    }

    @Test (expected = ProvisioningDeviceClientException.class)
    public void constructorThrowsOnNullAuth() throws ProvisioningDeviceClientException
    {
        //act
        StatusTask statusTask = Deencapsulation.newInstance(StatusTask.class, new Class[] {SecurityClient.class,
                                                                    ProvisioningDeviceClientContract.class, String.class,
                                                                    Authorization.class},
                                                            mockedSecurityClient, mockedProvisioningDeviceClientContract,
                                                            TEST_OPERATION_ID, null);
    }

    @Test
    public void getRegistrationStatusSucceeds() throws Exception
    {
        //arrange
        StatusTask statusTask = Deencapsulation.newInstance(StatusTask.class, new Class[] {SecurityClient.class,
                                                                    ProvisioningDeviceClientContract.class, String.class,
                                                                    Authorization.class},
                                                            mockedSecurityClient, mockedProvisioningDeviceClientContract,
                                                            TEST_OPERATION_ID, mockedAuthorization);
        new NonStrictExpectations()
        {
            {
                mockedSecurityClient.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                Deencapsulation.invoke(mockedAuthorization, "getSslContext");
                result = mockedSslContext;
                Deencapsulation.invoke(mockedResponseData, "getResponseData");
                result = "NonNullValue".getBytes();
                Deencapsulation.invoke(mockedResponseData, "getContractState");
                result = DPS_REGISTRATION_RECEIVED;
                ResponseParser.createFromJson(anyString);
                result = mockedResponseParser;
            }
        };

        //act
        statusTask.call();
        //assert
        new Verifications()
        {
            {
                mockedProvisioningDeviceClientContract.getRegistrationStatus(TEST_OPERATION_ID, TEST_REGISTRATION_ID,
                                                                             anyString, mockedSslContext,
                                                                             (ResponseCallback)any, any);
                times = 1;
            }
        };
    }

    //SRS_StatusTask_25_003: [ This method shall throw ProvisioningDeviceClientException if registration id is null or empty. ]
    @Test (expected = ProvisioningDeviceSecurityException.class)
    public void getRegistrationStatusThrowsOnNullRegId() throws Exception
    {
        //arrange
        StatusTask statusTask = Deencapsulation.newInstance(StatusTask.class, new Class[] {SecurityClient.class,
                                                                    ProvisioningDeviceClientContract.class, String.class,
                                                                    Authorization.class},
                                                            mockedSecurityClient, mockedProvisioningDeviceClientContract,
                                                            TEST_OPERATION_ID, mockedAuthorization);
        new NonStrictExpectations()
        {
            {
                mockedSecurityClient.getRegistrationId();
                result = null;
            }
        };

        //act
        statusTask.call();
    }

    @Test (expected = ProvisioningDeviceSecurityException.class)
    public void getRegistrationStatusThrowsOnEmptyRegId() throws Exception
    {
        //arrange
        StatusTask statusTask = Deencapsulation.newInstance(StatusTask.class, new Class[] {SecurityClient.class,
                                                                    ProvisioningDeviceClientContract.class, String.class,
                                                                    Authorization.class},
                                                            mockedSecurityClient, mockedProvisioningDeviceClientContract,
                                                            TEST_OPERATION_ID, mockedAuthorization);
        new NonStrictExpectations()
        {
            {
                mockedSecurityClient.getRegistrationId();
                result = "";
            }
        };

        //act
        statusTask.call();
    }

    //SRS_StatusTask_25_004: [ This method shall retrieve the SSL context from Authorization and throw ProvisioningDeviceClientException if it is null. ]
    @Test (expected = ProvisioningDeviceSecurityException.class)
    public void getRegistrationStatusThrowsOnNullSslContext() throws Exception
    {
        //arrange
        StatusTask statusTask = Deencapsulation.newInstance(StatusTask.class, new Class[] {SecurityClient.class,
                                                                    ProvisioningDeviceClientContract.class, String.class,
                                                                    Authorization.class},
                                                            mockedSecurityClient, mockedProvisioningDeviceClientContract,
                                                            TEST_OPERATION_ID, mockedAuthorization);
        new NonStrictExpectations()
        {
            {
                mockedSecurityClient.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                Deencapsulation.invoke(mockedAuthorization, "getSslContext");
                result = null;
            }
        };

        //act
        statusTask.call();
    }

    //SRS_StatusTask_25_005: [ This method shall trigger getRegistrationStatus on the contract API and wait for response and return it. ]
    @Test (expected = IOException.class)
    public void getRegistrationStatusThrowsOnContractGetStatusFails() throws Exception
    {
        //arrange
        StatusTask statusTask = Deencapsulation.newInstance(StatusTask.class, new Class[] {SecurityClient.class,
                                                                    ProvisioningDeviceClientContract.class, String.class,
                                                                    Authorization.class},
                                                            mockedSecurityClient, mockedProvisioningDeviceClientContract,
                                                            TEST_OPERATION_ID, mockedAuthorization);
        new NonStrictExpectations()
        {
            {
                mockedSecurityClient.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                Deencapsulation.invoke(mockedAuthorization, "getSslContext");
                result = mockedSslContext;
                mockedProvisioningDeviceClientContract.getRegistrationStatus(TEST_OPERATION_ID, TEST_REGISTRATION_ID,
                                                                             anyString, mockedSslContext,
                                                                             (ResponseCallback)any, any);
                result = new IOException("testException");
            }
        };

        //act
        statusTask.call();
    }

    //SRS_StatusTask_25_006: [ This method shall throw ProvisioningDeviceClientException if null response or no response is received in maximum time of 90 seconds. ]
    @Test (expected = ProvisioningDeviceClientException.class)
    public void getRegistrationStatusThrowsIfNoResponseReceivedInMaxTime() throws Exception
    {
        //arrange
        StatusTask statusTask = Deencapsulation.newInstance(StatusTask.class, new Class[] {SecurityClient.class,
                                                                    ProvisioningDeviceClientContract.class, String.class,
                                                                    Authorization.class},
                                                            mockedSecurityClient, mockedProvisioningDeviceClientContract,
                                                            TEST_OPERATION_ID, mockedAuthorization);
        new NonStrictExpectations()
        {
            {
                mockedSecurityClient.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                Deencapsulation.invoke(mockedAuthorization, "getSslContext");
                result = mockedSslContext;
            }
        };

        //act
        statusTask.call();
    }

    @Test (expected = ProvisioningDeviceClientException.class)
    public void getRegistrationStatusThrowsOnNullResponseReceived() throws Exception
    {
        //arrange
        StatusTask statusTask = Deencapsulation.newInstance(StatusTask.class, new Class[] {SecurityClient.class,
                                                                    ProvisioningDeviceClientContract.class, String.class,
                                                                    Authorization.class},
                                                            mockedSecurityClient, mockedProvisioningDeviceClientContract,
                                                            TEST_OPERATION_ID, mockedAuthorization);
        new NonStrictExpectations()
        {
            {
                mockedSecurityClient.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                Deencapsulation.invoke(mockedAuthorization, "getSslContext");
                result = mockedSslContext;
                Deencapsulation.invoke(mockedResponseData, "getResponseData");
                result = null;
            }
        };

        //act
        statusTask.call();
    }

    @Test (expected = ProvisioningDeviceClientException.class)
    public void getRegistrationStatusThrowsOnInterruptedException() throws Exception
    {
        //arrange
        StatusTask statusTask = Deencapsulation.newInstance(StatusTask.class, new Class[] {SecurityClient.class,
                                                                    ProvisioningDeviceClientContract.class, String.class,
                                                                    Authorization.class},
                                                            mockedSecurityClient, mockedProvisioningDeviceClientContract,
                                                            TEST_OPERATION_ID, mockedAuthorization);
        new NonStrictExpectations()
        {
            {
                mockedSecurityClient.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                Deencapsulation.invoke(mockedAuthorization, "getSslContext");
                result = mockedSslContext;
                Deencapsulation.invoke(mockedResponseData, "getResponseData");
                result = "NonNullValue".getBytes();
                Deencapsulation.invoke(mockedResponseData, "getContractState");
                result = DPS_REGISTRATION_RECEIVED;
                ResponseParser.createFromJson(anyString);
                result = new InterruptedException();
            }
        };

        //act
        statusTask.call();
    }
}

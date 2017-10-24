/*
 *
 *  Copyright (c) Microsoft. All rights reserved.
 *  Licensed under the MIT license. See LICENSE file in the project root for full license information.
 *
 */

package tests.unit.com.microsoft.azure.sdk.iot.dps.device.internal.provisioningtask;

import com.microsoft.azure.sdk.iot.deps.util.Base64;
import com.microsoft.azure.sdk.iot.dps.security.DPSSecurityClient;
import com.microsoft.azure.sdk.iot.dps.security.DPSSecurityClientKey;
import com.microsoft.azure.sdk.iot.dps.security.DPSSecurityClientX509;
import com.microsoft.azure.sdk.iot.provisioning.device.ProvisioningDeviceClientConfig;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.contract.ProvisioningDeviceClientContract;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.contract.ResponseCallback;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.contract.UrlPathBuilder;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.exceptions.*;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.parser.RegisterRequestParser;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.parser.RegisterResponseTPMParser;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.parser.ResponseParser;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.provisioningtask.Authorization;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.provisioningtask.RegisterTask;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.provisioningtask.ResponseData;
import mockit.*;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.json.JsonException;
import javax.net.ssl.SSLContext;
import java.net.MalformedURLException;
import java.net.URLEncoder;

import static com.microsoft.azure.sdk.iot.provisioning.device.internal.provisioningtask.ContractState.DPS_REGISTRATION_RECEIVED;
import static com.microsoft.azure.sdk.iot.provisioning.device.internal.provisioningtask.ContractState.DPS_REGISTRATION_UNKNOWN;
import static org.junit.Assert.assertNotNull;

/*
    Unit Test for Register Task
    Coverage : 88% Method, 92% Line (private classes are non testable)
 */
@RunWith(JMockit.class)
public class RegisterTaskTest
{
    private static final String TEST_REGISTRATION_ID = "testRegistrationId";
    private static final String TEST_EK = "testEK";
    private static final String TEST_SRK = "testSRK";
    private static final String TEST_AUTH_KEY = "testAuthKey";

    @Mocked
    DPSSecurityClient mockedDpsSecurityClient;
    @Mocked
    DPSSecurityClientKey mockedDpsSecurityClientKey;
    @Mocked
    DPSSecurityClientX509 mockedDpsSecurityClientX509;
    @Mocked
    ProvisioningDeviceClientContract mockedProvisioningDeviceClientContract;
    @Mocked
    ProvisioningDeviceClientConfig mockedProvisioningDeviceClientConfig;
    @Mocked
    Authorization mockedAuthorization;
    @Mocked
    RegisterRequestParser mockedRegisterRequestParser;
    @Mocked
    UrlPathBuilder mockedUrlPathBuilder;
    @Mocked
    Base64 mockedBase64;
    @Mocked
    URLEncoder mockedUrlEncoder;
    @Mocked
    RegisterResponseTPMParser mockedRegisterResponseTPMParser;
    @Mocked
    SSLContext mockedSslContext;
    @Mocked
    ResponseParser mockedResponseParser;
    @Mocked
    ResponseCallback mockedResponseCallback;
    @Mocked
    ResponseData mockedResponseData;

    //SRS_RegisterTask_25_001: [ Constructor shall save provisioningDeviceClientConfig , dpsSecurityClient, provisioningDeviceClientContract and authorization.]
    @Test
    public void constructorSucceeds() throws ProvisioningDeviceClientException
    {
        //arrange

        //act
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig, mockedDpsSecurityClient,
                     mockedProvisioningDeviceClientContract, mockedAuthorization);
        //assert
        assertNotNull(Deencapsulation.getField(registerTask, "provisioningDeviceClientConfig"));
        assertNotNull(Deencapsulation.getField(registerTask, "dpsSecurityClient"));
        assertNotNull(Deencapsulation.getField(registerTask, "provisioningDeviceClientContract"));
        assertNotNull(Deencapsulation.getField(registerTask, "authorization"));
        assertNotNull(Deencapsulation.getField(registerTask, "responseCallback"));
    }

    //SRS_RegisterTask_25_002: [ Constructor throw ProvisioningDeviceClientException if provisioningDeviceClientConfig , dpsSecurityClient, authorization or provisioningDeviceClientContract is null.]
    @Test (expected = ProvisioningDeviceClientException.class)
    public void constructorThrowsOnNullConfig() throws ProvisioningDeviceClientException
    {
        //arrange
        //act
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, new Class[]{ProvisioningDeviceClientConfig.class,
                                                                        DPSSecurityClient.class, ProvisioningDeviceClientContract.class,
                                                                        Authorization.class},
                                                                null, mockedDpsSecurityClient,
                                                                mockedProvisioningDeviceClientContract, mockedAuthorization);
        //assert
    }

    @Test (expected = ProvisioningDeviceClientException.class)
    public void constructorThrowsOnNullSecurityClient() throws ProvisioningDeviceClientException
    {
        //arrange
        //act
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, new Class[]{ProvisioningDeviceClientConfig.class,
                                                                        DPSSecurityClient.class, ProvisioningDeviceClientContract.class,
                                                                        Authorization.class},
                                                                mockedProvisioningDeviceClientConfig, null,
                                                                mockedProvisioningDeviceClientContract, mockedAuthorization);
        //assert
    }

    @Test (expected = ProvisioningDeviceClientException.class)
    public void constructorThrowsOnNullContract() throws ProvisioningDeviceClientException
    {
        //act
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, new Class[]{ProvisioningDeviceClientConfig.class,
                                                                        DPSSecurityClient.class, ProvisioningDeviceClientContract.class,
                                                                        Authorization.class},
                                                                mockedProvisioningDeviceClientConfig, mockedDpsSecurityClient,
                                                                null, mockedAuthorization);
    }

    @Test (expected = ProvisioningDeviceClientException.class)
    public void constructorThrowsOnNullAuthorization() throws ProvisioningDeviceClientException
    {
        //act
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, new Class[]{ProvisioningDeviceClientConfig.class,
                                                                        DPSSecurityClient.class, ProvisioningDeviceClientContract.class,
                                                                        Authorization.class},
                                                                mockedProvisioningDeviceClientConfig, mockedDpsSecurityClient,
                                                                mockedProvisioningDeviceClientContract, null);
    }

    //SRS_RegisterTask_25_006: [ If the provided security client is for X509 then, this method shall trigger authenticateWithDPS on the contract API and wait for response and return it. ]
    @Test
    public void authenticateWithX509Succeeds() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientX509, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientX509.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientX509.getSSLContext();
                result = mockedSslContext;
                Deencapsulation.invoke(mockedResponseData, "getResponseData");
                result = "NonNullValue".getBytes();
                Deencapsulation.invoke(mockedResponseData, "getContractState");
                result = DPS_REGISTRATION_RECEIVED;
            }
        };
        //act
        registerTask.call();

        //assert
        new Verifications()
        {
            {
                Deencapsulation.invoke(mockedAuthorization, "setSslContext", mockedSslContext);
                times = 1;
                mockedProvisioningDeviceClientContract.authenticateWithDPS((byte[])any, anyString,
                                                                           mockedSslContext, null,
                                                                           (ResponseCallback)any, any);
                times = 1;
            }
        };
    }

    @Test (expected = ProvisioningDeviceClientException.class)
    public void authenticateWithX509ThrowsOnNonExistentType() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClient, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        //act
        registerTask.call();
    }


    //SRS_RegisterTask_25_003: [ If the provided security client is for X509 then, this method shall throw ProvisioningDeviceClientException if registration id is null. ]
    @Test (expected = ProvisioningDeviceClientException.class)
    public void authenticateWithX509ThrowsOnNullRegId() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientX509, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientX509.getRegistrationId();
                result = null;
            }
        };
        //act
        registerTask.call();

    }

    //SRS_RegisterTask_25_005: [ If the provided security client is for X509 then, this method shall build the required Json input using parser. ]
    @Test(expected = JsonException.class)
    public void authenticateWithX509ThrowsOnParserFailure() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientX509, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientX509.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedRegisterRequestParser.toJson();
                result = new JsonException("test");

            }
        };
        //act
        registerTask.call();
    }

    //SRS_RegisterTask_25_004: [ If the provided security client is for X509 then, this method shall save the SSL context to Authorization if it is not null and throw ProvisioningDeviceClientException otherwise. ]

    @Test (expected = ProvisioningDeviceSecurityException.class)
    public void authenticateWithX509ThrowsOnNullSSLContext() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientX509, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientX509.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientX509.getSSLContext();
                result = null;
            }
        };
        //act
        registerTask.call();
    }

    //SRS_RegisterTask_25_007: [ If the provided security client is for X509 then, this method shall throw ProvisioningDeviceClientException if null response is received. ]
    @Test (expected = ProvisioningDeviceTransportException.class)
    public void authenticateWithX509ThrowsOnAuthenticateWithDPSFail() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientX509, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientX509.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientX509.getSSLContext();
                result = mockedSslContext;
                mockedProvisioningDeviceClientContract.authenticateWithDPS((byte[])any, anyString,
                                                                           mockedSslContext, null,
                                                                           (ResponseCallback)any, any);
                result = new ProvisioningDeviceTransportException("test transport exception");
            }
        };
        //act
        registerTask.call();
    }

    @Test (expected = ProvisioningDeviceClientException.class)
    public void authenticateWithX509ThrowsIfNoResponseReceivedInMaxTime() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientX509, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientX509.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientX509.getSSLContext();
                result = mockedSslContext;
            }
        };
        //act
        registerTask.call();
    }

    @Test (expected = ProvisioningDeviceClientException.class)
    public void authenticateWithX509ThrowsIfNullResponseReceivedInMaxTime() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientX509, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientX509.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientX509.getSSLContext();
                result = mockedSslContext;
                Deencapsulation.invoke(mockedResponseData, "getResponseData");
                result = null;
                Deencapsulation.invoke(mockedResponseData, "getContractState");
                result = DPS_REGISTRATION_UNKNOWN;
            }
        };
        //act
        registerTask.call();
    }

    @Test (expected = ProvisioningDeviceClientException.class)
    public void authenticateWithX509ThrowsOnThreadInterruptedException() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientX509, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientX509.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientX509.getSSLContext();
                result = new InterruptedException();
            }
        };
        //act
        registerTask.call();
    }

    @Test
    public void authenticateWithSasTokenSucceeds() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientKey, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientKey.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedDpsSecurityClientKey.getDeviceEk();
                result = TEST_EK.getBytes();
                mockedDpsSecurityClientKey.getDeviceSRK();
                result = TEST_SRK.getBytes();
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientKey.getSSLContext();
                result = mockedSslContext;
                Deencapsulation.invoke(mockedResponseData, "getResponseData");
                result = "NonNullValue".getBytes();
                Deencapsulation.invoke(mockedResponseData, "getContractState");
                result = DPS_REGISTRATION_RECEIVED;
                mockedRegisterResponseTPMParser.getAuthenticationKey();
                result = TEST_AUTH_KEY;
                mockedUrlPathBuilder.generateSasTokenUrl(TEST_REGISTRATION_ID);
                result = "testUrl";
                mockedDpsSecurityClientKey.signData((byte[])any);
                result = "testToken".getBytes();
            }
        };
        //act
        registerTask.call();

        //assert
        new Verifications()
        {
            {
                Deencapsulation.invoke(mockedAuthorization, "setSslContext", mockedSslContext);
                times = 1;
                Deencapsulation.invoke(mockedAuthorization, "setSasToken", anyString);
                times = 1;
                mockedProvisioningDeviceClientContract.requestNonceWithDPSTPM((byte[])any, anyString,
                                                                              mockedSslContext,
                                                                              (ResponseCallback)any, any);
                times = 1;
                mockedDpsSecurityClientKey.importKey((byte[])any);
                times = 1;
                mockedProvisioningDeviceClientContract.authenticateWithDPS((byte[])any, anyString,
                                                                           mockedSslContext, anyString,
                                                                           (ResponseCallback)any, any);
                times = 1;
            }
        };
    }

    //SRS_RegisterTask_25_008: [ If the provided security client is for Key then, this method shall throw ProvisioningDeviceClientException if registration id or endorsement key or storage root key are null. ]
    @Test (expected = ProvisioningDeviceClientException.class)
    public void authenticateWithSasTokenNonceThrowsOnNullRegId() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientKey, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientKey.getRegistrationId();
                result = null;
            }
        };
        //act
        registerTask.call();
    }

    @Test (expected = ProvisioningDeviceSecurityException.class)
    public void authenticateWithSasTokenNonceThrowsOnNullEk() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientKey, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientKey.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedDpsSecurityClientKey.getDeviceEk();
                result = null;
            }
        };
        //act
        registerTask.call();
    }

    @Test (expected = ProvisioningDeviceSecurityException.class)
    public void authenticateWithSasTokenNonceThrowsOnNullSRK() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientKey, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientKey.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedDpsSecurityClientKey.getDeviceEk();
                result = TEST_EK.getBytes();
                mockedDpsSecurityClientKey.getDeviceSRK();
                result = null;
            }
        };
        //act
        registerTask.call();
    }

    //SRS_RegisterTask_25_010: [ If the provided security client is for Key then, this method shall build the required Json input with base 64 encoded endorsement key, storage root key and on failure pass the exception back to the user. ]
    @Test (expected = JsonException.class)
    public void authenticateWithSasTokenNonceThrowsOnParserFailure() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientKey, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientKey.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedDpsSecurityClientKey.getDeviceEk();
                result = TEST_EK.getBytes();
                mockedDpsSecurityClientKey.getDeviceSRK();
                result = TEST_SRK.getBytes();
                mockedRegisterRequestParser.toJson();
                result = new JsonException("testException");
            }
        };
        //act
        registerTask.call();
    }

    //SRS_RegisterTask_25_009: [ If the provided security client is for Key then, this method shall save the SSL context to Authorization if it is not null and throw ProvisioningDeviceClientException otherwise. ]
    @Test (expected = ProvisioningDeviceSecurityException.class)
    public void authenticateWithSasTokenNonceThrowsOnNullSSLContext() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientKey, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientKey.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedDpsSecurityClientKey.getDeviceEk();
                result = TEST_EK.getBytes();
                mockedDpsSecurityClientKey.getDeviceSRK();
                result = TEST_SRK.getBytes();
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientKey.getSSLContext();
                result = null;
            }
        };
        //act
        registerTask.call();
    }

    //SRS_RegisterTask_25_011: [ If the provided security client is for Key then, this method shall trigger requestNonceWithDPSTPM on the contract API and wait for Authentication Key and decode it from Base64. Also this method shall pass the exception back to the user if it fails. ]
    @Test (expected = ProvisioningDeviceHubException.class)
    public void authenticateWithSasTokenNonceThrowsOnRequestNonceFail() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientKey, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientKey.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedDpsSecurityClientKey.getDeviceEk();
                result = TEST_EK.getBytes();
                mockedDpsSecurityClientKey.getDeviceSRK();
                result = TEST_SRK.getBytes();
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientKey.getSSLContext();
                result = mockedSslContext;
                mockedProvisioningDeviceClientContract.requestNonceWithDPSTPM((byte[])any, anyString,
                                                                              mockedSslContext,
                                                                              (ResponseCallback)any, any);
                result = new ProvisioningDeviceHubException("test exception");
            }
        };
        //act
        registerTask.call();
    }

    //SRS_RegisterTask_25_012: [ If the provided security client is for Key then, this method shall throw ProvisioningDeviceClientException if null response is received. ]
    @Test (expected = ProvisioningDeviceClientException.class)
    public void authenticateWithSasTokenNonceThrowsIfNoResponseReceivedInMaxTimeForNonce() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientKey, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientKey.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedDpsSecurityClientKey.getDeviceEk();
                result = TEST_EK.getBytes();
                mockedDpsSecurityClientKey.getDeviceSRK();
                result = TEST_SRK.getBytes();
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientKey.getSSLContext();
                result = mockedSslContext;
            }
        };
        //act
        registerTask.call();
    }

    @Test (expected = ProvisioningDeviceClientException.class)
    public void authenticateWithSasTokenNonceThrowsIfNullResponseReceivedInMaxTimeForNonce() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientKey, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientKey.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedDpsSecurityClientKey.getDeviceEk();
                result = TEST_EK.getBytes();
                mockedDpsSecurityClientKey.getDeviceSRK();
                result = TEST_SRK.getBytes();
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientKey.getSSLContext();
                result = mockedSslContext;
                Deencapsulation.invoke(mockedResponseData, "getResponseData");
                result = null;
            }
        };
        //act
        registerTask.call();
    }

    @Test (expected = ProvisioningDeviceClientException.class)
    public void authenticateWithSasTokenThrowsOnThreadInterruptedException() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientKey, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientKey.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedDpsSecurityClientKey.getDeviceEk();
                result = TEST_EK.getBytes();
                mockedDpsSecurityClientKey.getDeviceSRK();
                result = TEST_SRK.getBytes();
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientKey.getSSLContext();
                result = new InterruptedException();
            }
        };
        //act
        registerTask.call();
    }

    //SRS_RegisterTask_25_013: [ If the provided security client is for Key then, this method shall throw ProvisioningDeviceClientException if Authentication Key received is null. ]
    @Test (expected = ProvisioningDeviceClientAuthenticationException.class)
    public void authenticateWithSasTokenThrowsOnNullAuthKey() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientKey, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientKey.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedDpsSecurityClientKey.getDeviceEk();
                result = TEST_EK.getBytes();
                mockedDpsSecurityClientKey.getDeviceSRK();
                result = TEST_SRK.getBytes();
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientKey.getSSLContext();
                result = mockedSslContext;
                Deencapsulation.invoke(mockedResponseData, "getResponseData");
                result = "NonNullValue".getBytes();
                Deencapsulation.invoke(mockedResponseData, "getContractState");
                result = DPS_REGISTRATION_RECEIVED;
                mockedRegisterResponseTPMParser.getAuthenticationKey();
                result = null;
            }
        };
        //act
        registerTask.call();
    }

    @Test (expected = JsonException.class)
    public void authenticateWithSasTokenThrowsOnParserFailure() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientKey, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientKey.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedDpsSecurityClientKey.getDeviceEk();
                result = TEST_EK.getBytes();
                mockedDpsSecurityClientKey.getDeviceSRK();
                result = TEST_SRK.getBytes();
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientKey.getSSLContext();
                result = mockedSslContext;
                Deencapsulation.invoke(mockedResponseData, "getResponseData");
                result = "NonNullValue".getBytes();
                Deencapsulation.invoke(mockedResponseData, "getContractState");
                result = DPS_REGISTRATION_RECEIVED;
                RegisterResponseTPMParser.createFromJson(anyString);
                result = new JsonException("test exception");
            }
        };
        //act
        registerTask.call();

    }

    //SRS_RegisterTask_25_018: [ If the provided security client is for Key then, this method shall import the Base 64 encoded Authentication Key into the HSM using the security client and pass the exception to the user on failure. ]
    @Test (expected = ProvisioningDeviceClientException.class)
    public void authenticateWithSasTokenThrowsImportKeyFailure() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientKey, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientKey.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedDpsSecurityClientKey.getDeviceEk();
                result = TEST_EK.getBytes();
                mockedDpsSecurityClientKey.getDeviceSRK();
                result = TEST_SRK.getBytes();
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientKey.getSSLContext();
                result = mockedSslContext;
                Deencapsulation.invoke(mockedResponseData, "getResponseData");
                result = "NonNullValue".getBytes();
                Deencapsulation.invoke(mockedResponseData, "getContractState");
                result = DPS_REGISTRATION_RECEIVED;
                mockedRegisterResponseTPMParser.getAuthenticationKey();
                result = TEST_AUTH_KEY;
                mockedDpsSecurityClientKey.importKey((byte[])any);
                result = new ProvisioningDeviceClientException("test exception");
            }
        };
        //act
        registerTask.call();
    }

    /*SRS_RegisterTask_25_014: [ If the provided security client is for Key then, this method shall construct SasToken by doing the following

            1. Build a tokenScope of format <scopeid>/registrations/<registrationId>
            2. Sign the HSM with the string of format <tokenScope>/n<expiryTime> and receive a token
            3. Encode the token to Base64 format and UrlEncode it to generate the signature. ]*/
    @Test (expected = ProvisioningDeviceClientException.class)
    public void authenticateWithSasTokenThrowsConstructTokenFailure() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientKey, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientKey.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedDpsSecurityClientKey.getDeviceEk();
                result = TEST_EK.getBytes();
                mockedDpsSecurityClientKey.getDeviceSRK();
                result = TEST_SRK.getBytes();
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientKey.getSSLContext();
                result = mockedSslContext;
                Deencapsulation.invoke(mockedResponseData, "getResponseData");
                result = "NonNullValue".getBytes();
                Deencapsulation.invoke(mockedResponseData, "getContractState");
                result = DPS_REGISTRATION_RECEIVED;
                mockedRegisterResponseTPMParser.getAuthenticationKey();
                result = TEST_AUTH_KEY;
                mockedUrlPathBuilder.generateSasTokenUrl(TEST_REGISTRATION_ID);
                result = new MalformedURLException("test exception");
            }
        };
        //act
        registerTask.call();

    }

    @Test (expected = ProvisioningDeviceClientException.class)
    public void authenticateWithSasTokenThrowsUrlBuilderReturnsNull() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientKey, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientKey.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedDpsSecurityClientKey.getDeviceEk();
                result = TEST_EK.getBytes();
                mockedDpsSecurityClientKey.getDeviceSRK();
                result = TEST_SRK.getBytes();
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientKey.getSSLContext();
                result = mockedSslContext;
                Deencapsulation.invoke(mockedResponseData, "getResponseData");
                result = "NonNullValue".getBytes();
                Deencapsulation.invoke(mockedResponseData, "getContractState");
                result = DPS_REGISTRATION_RECEIVED;
                mockedRegisterResponseTPMParser.getAuthenticationKey();
                result = TEST_AUTH_KEY;
                mockedUrlPathBuilder.generateSasTokenUrl(TEST_REGISTRATION_ID);
                result = null;
            }
        };
        //act
        registerTask.call();
    }

    @Test (expected = ProvisioningDeviceClientException.class)
    public void authenticateWithSasTokenThrowsUrlBuilderReturnsEmpty() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientKey, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientKey.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedDpsSecurityClientKey.getDeviceEk();
                result = TEST_EK.getBytes();
                mockedDpsSecurityClientKey.getDeviceSRK();
                result = TEST_SRK.getBytes();
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientKey.getSSLContext();
                result = mockedSslContext;
                Deencapsulation.invoke(mockedResponseData, "getResponseData");
                result = "NonNullValue".getBytes();
                Deencapsulation.invoke(mockedResponseData, "getContractState");
                result = DPS_REGISTRATION_RECEIVED;
                mockedRegisterResponseTPMParser.getAuthenticationKey();
                result = TEST_AUTH_KEY;
                mockedUrlPathBuilder.generateSasTokenUrl(TEST_REGISTRATION_ID);
                result = "";
            }
        };
        //act
        registerTask.call();
    }

    @Test (expected = ProvisioningDeviceClientException.class)
    public void authenticateWithSasTokenThrowsConstructTokenSignDataFailure() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientKey, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientKey.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedDpsSecurityClientKey.getDeviceEk();
                result = TEST_EK.getBytes();
                mockedDpsSecurityClientKey.getDeviceSRK();
                result = TEST_SRK.getBytes();
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientKey.getSSLContext();
                result = mockedSslContext;
                Deencapsulation.invoke(mockedResponseData, "getResponseData");
                result = "NonNullValue".getBytes();
                Deencapsulation.invoke(mockedResponseData, "getContractState");
                result = DPS_REGISTRATION_RECEIVED;
                mockedRegisterResponseTPMParser.getAuthenticationKey();
                result = TEST_AUTH_KEY;
                mockedUrlPathBuilder.generateSasTokenUrl(TEST_REGISTRATION_ID);
                result = "testUrl";
                mockedDpsSecurityClientKey.signData((byte[])any);
                result = new ProvisioningDeviceClientException("test Exception");
            }
        };
        //act
        registerTask.call();

    }

    @Test (expected = ProvisioningDeviceClientException.class)
    public void authenticateWithSasTokenThrowsConstructTokenSignDataReturnsNull() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientKey, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientKey.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedDpsSecurityClientKey.getDeviceEk();
                result = TEST_EK.getBytes();
                mockedDpsSecurityClientKey.getDeviceSRK();
                result = TEST_SRK.getBytes();
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientKey.getSSLContext();
                result = mockedSslContext;
                Deencapsulation.invoke(mockedResponseData, "getResponseData");
                result = "NonNullValue".getBytes();
                Deencapsulation.invoke(mockedResponseData, "getContractState");
                result = DPS_REGISTRATION_RECEIVED;
                mockedRegisterResponseTPMParser.getAuthenticationKey();
                result = TEST_AUTH_KEY;
                mockedUrlPathBuilder.generateSasTokenUrl(TEST_REGISTRATION_ID);
                result = "testUrl";
                mockedDpsSecurityClientKey.signData((byte[])any);
                result = null;
            }
        };
        //act
        registerTask.call();

    }

    @Test (expected = ProvisioningDeviceClientException.class)
    public void authenticateWithSasTokenThrowsConstructTokenSignDataReturnsEmpty() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientKey, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientKey.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedDpsSecurityClientKey.getDeviceEk();
                result = TEST_EK.getBytes();
                mockedDpsSecurityClientKey.getDeviceSRK();
                result = TEST_SRK.getBytes();
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientKey.getSSLContext();
                result = mockedSslContext;
                Deencapsulation.invoke(mockedResponseData, "getResponseData");
                result = "NonNullValue".getBytes();
                Deencapsulation.invoke(mockedResponseData, "getContractState");
                result = DPS_REGISTRATION_RECEIVED;
                mockedRegisterResponseTPMParser.getAuthenticationKey();
                result = TEST_AUTH_KEY;
                mockedUrlPathBuilder.generateSasTokenUrl(TEST_REGISTRATION_ID);
                result = "testUrl";
                mockedDpsSecurityClientKey.signData((byte[])any);
                result = "".getBytes();
            }
        };
        //act
        registerTask.call();

    }

    //SRS_RegisterTask_25_016: [ If the provided security client is for Key then, this method shall trigger authenticateWithDPS on the contract API using the sasToken generated and wait for response and return it. ]
    @Test (expected = ProvisioningDeviceTransportException.class)
    public void authenticateWithSasTokenThrowsOnAuthenticateWithDPSFail() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientKey, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientKey.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedDpsSecurityClientKey.getDeviceEk();
                result = TEST_EK.getBytes();
                mockedDpsSecurityClientKey.getDeviceSRK();
                result = TEST_SRK.getBytes();
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientKey.getSSLContext();
                result = mockedSslContext;
                Deencapsulation.invoke(mockedResponseData, "getResponseData");
                result = "NonNullValue".getBytes();
                Deencapsulation.invoke(mockedResponseData, "getContractState");
                result = DPS_REGISTRATION_RECEIVED;
                mockedRegisterResponseTPMParser.getAuthenticationKey();
                result = TEST_AUTH_KEY;
                mockedUrlPathBuilder.generateSasTokenUrl(TEST_REGISTRATION_ID);
                result = "testUrl";
                mockedDpsSecurityClientKey.signData((byte[])any);
                result = "testToken".getBytes();
                mockedProvisioningDeviceClientContract.authenticateWithDPS((byte[])any, anyString,
                                                                           mockedSslContext, null,
                                                                           (ResponseCallback)any, any);
                result = new ProvisioningDeviceTransportException("test transport exception");
            }
        };
        //act
        registerTask.call();
    }

    //SRS_RegisterTask_25_017: [ If the provided security client is for Key then, this method shall throw ProvisioningDeviceClientException if null response to authenticateWithDPS is received. ]
    @Test (expected = ProvisioningDeviceClientAuthenticationException.class)
    public void authenticateWithSasTokenThrowsIfNoResponseReceivedInMaxTime() throws Exception
    {
        //arrange
        RegisterTask registerTask = Deencapsulation.newInstance(RegisterTask.class, mockedProvisioningDeviceClientConfig,
                                                                mockedDpsSecurityClientKey, mockedProvisioningDeviceClientContract,
                                                                mockedAuthorization);

        new NonStrictExpectations()
        {
            {
                mockedDpsSecurityClientKey.getRegistrationId();
                result = TEST_REGISTRATION_ID;
                mockedDpsSecurityClientKey.getDeviceEk();
                result = TEST_EK.getBytes();
                mockedDpsSecurityClientKey.getDeviceSRK();
                result = TEST_SRK.getBytes();
                mockedRegisterRequestParser.toJson();
                result = "testJson";
                mockedDpsSecurityClientKey.getSSLContext();
                result = mockedSslContext;
                mockedProvisioningDeviceClientContract.requestNonceWithDPSTPM((byte[]) any, anyString,
                                                                              mockedSslContext, (ResponseCallback)any,
                                                                              any);
            }
        };

        new StrictExpectations()
        {
            {
                Deencapsulation.newInstance(ResponseData.class);
                result = mockedResponseData;
                Deencapsulation.invoke(mockedResponseData, "getResponseData");
                result = "NonNullValue".getBytes();
                Deencapsulation.invoke(mockedResponseData, "getContractState");
                result = DPS_REGISTRATION_RECEIVED;
                Deencapsulation.invoke(mockedResponseData, "getResponseData");
                result = "NonNullValue".getBytes();
                Deencapsulation.invoke(mockedResponseData, "getContractState");
                result = DPS_REGISTRATION_RECEIVED;
                Deencapsulation.invoke(mockedResponseData, "getResponseData");
                result = "NonNullValue".getBytes();
            }
        };

        new NonStrictExpectations()
        {
            {
                mockedRegisterResponseTPMParser.getAuthenticationKey();
                result = TEST_AUTH_KEY;
                mockedUrlPathBuilder.generateSasTokenUrl(TEST_REGISTRATION_ID);
                result = "testUrl";
                mockedDpsSecurityClientKey.signData((byte[])any);
                result = "testToken".getBytes();
                mockedProvisioningDeviceClientContract.authenticateWithDPS((byte[])any, anyString,
                                                                           mockedSslContext, null,
                                                                           (ResponseCallback)any, any);
            }
        };

        new StrictExpectations()
        {
            {
                Deencapsulation.newInstance(ResponseData.class);
                result = mockedResponseData;
                Deencapsulation.invoke(mockedResponseData, "getResponseData");
                result = null;
                Deencapsulation.invoke(mockedResponseData, "getResponseData");
                result = null;
            }
        };
        //act
        registerTask.call();
    }
}

/*
 *
 *  Copyright (c) Microsoft. All rights reserved.
 *  Licensed under the MIT license. See LICENSE file in the project root for full license information.
 *
 */

package com.microsoft.azure.sdk.iot.provisioning.device.internal.contract;

import com.microsoft.azure.sdk.iot.provisioning.device.ProvisioningDeviceClientConfig;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.contract.http.ContractAPIHttp;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.exceptions.ProvisioningDeviceClientException;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.exceptions.ProvisioningDeviceHubException;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.exceptions.ProvisioningDeviceTransportException;

import javax.net.ssl.SSLContext;

public abstract class ProvisioningDeviceClientContract
{
    /**
     * Static method to create contracts with the service over the specified protocol
     * @param provisioningDeviceClientConfig Config used to specify details of the service
     * @return Implementation of the relevant contract for the requested protocol
     * @throws ProvisioningDeviceClientException This exception is thrown if the contract implementation could not be instantiated.
     */
    public static ProvisioningDeviceClientContract createProvisioningContract(ProvisioningDeviceClientConfig provisioningDeviceClientConfig) throws ProvisioningDeviceClientException
    {
        if (provisioningDeviceClientConfig == null)
        {
            throw new ProvisioningDeviceClientException("config cannot be null");
        }
        switch (provisioningDeviceClientConfig.getProtocol())
        {
            case MQTT:
                return null;

            case MQTT_WS:
                return null;

            case AMQPS:
                return null;

            case AMQPS_WS:
                return null;

            case HTTPS:
                return new ContractAPIHttp(provisioningDeviceClientConfig.getDpsScopeId(), provisioningDeviceClientConfig.getDpsURI());

            default:
                throw new ProvisioningDeviceClientException("Unknown protocol");
        }
    }

    public abstract void requestNonceForTPM(byte[] payload, String registrationId, SSLContext sslContext, ResponseCallback responseCallback, Object dpsAuthorizationCallbackContext) throws ProvisioningDeviceClientException, ProvisioningDeviceTransportException, ProvisioningDeviceHubException;
    public abstract void authenticateWithProvisioningService(byte[] payload, String registrationId, SSLContext sslContext, String authorization, ResponseCallback responseCallback, Object dpsAuthorizationCallbackContext) throws ProvisioningDeviceClientException, ProvisioningDeviceTransportException, ProvisioningDeviceHubException;
    public abstract void getRegistrationStatus(String operationId, String registrationId, String dpsAuthorization, SSLContext sslContext, ResponseCallback responseCallback, Object dpsAuthorizationCallbackContext) throws ProvisioningDeviceClientException, ProvisioningDeviceTransportException, ProvisioningDeviceHubException;
}

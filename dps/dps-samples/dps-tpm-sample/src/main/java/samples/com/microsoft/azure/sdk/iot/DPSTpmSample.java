/*
 *
 *  Copyright (c) Microsoft. All rights reserved.
 *  Licensed under the MIT license. See LICENSE file in the project root for full license information.
 *
 */

package samples.com.microsoft.azure.sdk.iot;

import com.microsoft.azure.sdk.iot.provisioning.device.*;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.exceptions.ProvisioningDeviceClientException;
import com.microsoft.azure.sdk.iot.dps.security.DPSHsmType;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

/**
 * Device Twin Sample for an IoT Hub. Default protocol is to use
 * MQTT transport.
 */
public class DPSTpmSample
{
    //private static final String scopeId = "[Your scope ID here]";
    //private static final String scopeId = "0ne00000020";
    private static final String scopeId = "0NE3F78B3C0";
    //private static final String dpsUri = "[Your DPS HUB here]";
    //private static final String dpsUri = "global.azure-devices-provisioning.net";
    //private static final String dpsUri = "global.df.azure-devices-provisioning-int.net";
    private static final String dpsUri = "dpspp9.azure-devices-provisioning-int.net";
    private static final ProvisioningDeviceClientTransportProtocol PROVISIONING_DEVICE_CLIENT_TRANSPORT_PROTOCOL = ProvisioningDeviceClientTransportProtocol.HTTPS;

    private static ProvisioningDeviceClientRegistrationInfo provisioningDeviceClientRegistrationInfoClient = new ProvisioningDeviceClientRegistrationInfo();
    private static final int MAX_TIME_TO_WAIT_FOR_DPS_REGISTRATION = 1000; // in milli seconds

    static class DPSStatus
    {
        ProvisioningDeviceClientStatus status;
        String reason;
    }

    static class ProvisioningDeviceClientStatusCallbackImpl implements ProvisioningDeviceClientStatusCallback
    {
        @Override
        public void run(ProvisioningDeviceClientStatus status, String reason, Object context)
        {
            System.out.println("DPS status " + status );
            if (reason != null)
            {
                System.out.println("because " + reason);
            }
            if (context instanceof DPSStatus)
            {
                DPSStatus dpsStatus = (DPSStatus) context;
                dpsStatus.status = status;
                dpsStatus.reason = reason;
            }
        }
    }

    static class ProvisioningDeviceClientRegistrationCallbackImpl implements ProvisioningDeviceClientRegistrationCallback
    {
        @Override
        public void run(ProvisioningDeviceClientRegistrationInfo provisioningDeviceClientRegistrationInfo, Object context)
        {
            if (context instanceof ProvisioningDeviceClientRegistrationInfo)
            {
                provisioningDeviceClientRegistrationInfoClient = provisioningDeviceClientRegistrationInfo;
            }
            else
            {
                System.out.println("Received unknown context");
            }
        }
    }

    public static void main(String[] args)
            throws IOException, URISyntaxException
    {
        System.out.println("Starting...");
        System.out.println("Beginning setup.");
        ProvisioningDeviceClient provisioningDeviceClient = null;
        try
        {
            DPSStatus dpsStatus = new DPSStatus();
            ProvisioningDeviceClientConfig provisioningDeviceClientConfig = new ProvisioningDeviceClientConfig(dpsUri, scopeId, PROVISIONING_DEVICE_CLIENT_TRANSPORT_PROTOCOL, DPSHsmType.TPM_EMULATOR);

            provisioningDeviceClient = new ProvisioningDeviceClient(provisioningDeviceClientConfig, new ProvisioningDeviceClientStatusCallbackImpl(), dpsStatus);

            provisioningDeviceClient.registerDevice(new ProvisioningDeviceClientRegistrationCallbackImpl(), provisioningDeviceClientRegistrationInfoClient);

            while (dpsStatus.status != ProvisioningDeviceClientStatus.DPS_DEVICE_STATUS_ASSIGNED)
            {
                if (dpsStatus.status == ProvisioningDeviceClientStatus.DPS_DEVICE_STATUS_ERROR)
                {
                    System.out.println("Dps error, bailing out");
                    break;
                }
                System.out.println("Waiting for Dps Hub to register");
                Thread.sleep(MAX_TIME_TO_WAIT_FOR_DPS_REGISTRATION);
            }

            if (provisioningDeviceClientRegistrationInfoClient.getDpsStatus() == ProvisioningDeviceClientStatus.DPS_DEVICE_STATUS_ASSIGNED)
            {
                System.out.println("IotHUb Uri : " + provisioningDeviceClientRegistrationInfoClient.getIothubUri());
                System.out.println("Device ID : " + provisioningDeviceClientRegistrationInfoClient.getDeviceId());
                // connect to iothub
            }
        }
        catch (ProvisioningDeviceClientException | InterruptedException e)
        {
            System.out.println("DPS threw a exception" + e.getMessage());
            if (provisioningDeviceClient != null)
            {
                provisioningDeviceClient.close();
            }
        }

        System.out.println("Press any key to exit...");

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        if (provisioningDeviceClient != null)
        {
            provisioningDeviceClient.close();
        }

        System.out.println("Shutting down...");

    }
}

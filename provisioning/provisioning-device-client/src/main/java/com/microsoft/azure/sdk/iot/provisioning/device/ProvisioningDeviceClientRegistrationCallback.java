/*
 *
 *  Copyright (c) Microsoft. All rights reserved.
 *  Licensed under the MIT license. See LICENSE file in the project root for full license information.
 *
 */

package com.microsoft.azure.sdk.iot.provisioning.device;

public interface ProvisioningDeviceClientRegistrationCallback
{
    void run(ProvisioningDeviceClientRegistrationInfo provisioningDeviceClientRegistrationInfo, Object context);
}

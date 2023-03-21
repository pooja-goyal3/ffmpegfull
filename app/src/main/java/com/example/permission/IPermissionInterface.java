/*****************************************************************
 *
 * BLUESKY CONFIDENTIAL
 * __________________
 *
 *  BS Inventions Pvt Ltd
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of BS Inventions Pvt Ltd and its partners,
 * if any. The intellectual and technical concepts contained
 * herein are proprietary to BS Inventions Pvt Ltd and partners.
 * Dissemination of this information or reproduction of this
 * material is strictly forbidden unless prior written
 * permission is obtained from BS Inventions Pvt Ltd.
 */
package com.example.permission;

import android.app.Activity;

import java.util.Map;


public interface IPermissionInterface {

    void onPermissionServed(int requestCode, boolean isPermissionGranted, Map<String, Object> map, Activity activity);
}

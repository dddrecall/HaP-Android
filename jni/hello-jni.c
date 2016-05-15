/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <string.h>
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <cpu-features.h>
/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
 */
 
 
jstring
Java_com_HaP_Byml_HelloJni_stringFromJNI( JNIEnv* env,
                                                  jobject thiz )
{
	
	  AndroidCpuFamily family;
    uint64_t features;
    char buffer[512];
		family = android_getCpuFamily();
    if ((family != ANDROID_CPU_FAMILY_ARM) &&
        (family != ANDROID_CPU_FAMILY_X86))
    {
        strlcat(buffer, "Not an ARM and not an X86 CPU !\n", sizeof buffer);
    }
		
   features = android_getCpuFeatures();
    if (((features & ANDROID_CPU_ARM_FEATURE_ARMv7) == 0) &&
        ((features & ANDROID_CPU_X86_FEATURE_SSSE3) == 0))
    {
        strlcat(buffer, "Not an ARMv7 and not an X86 SSSE3 CPU !\n", sizeof buffer);
    }

		
    return (*env)->NewStringUTF(env, buffer);
}


int 
Java_com_HaP_Byml_HelloJni_test( JNIEnv* env,
                                                  jobject thiz )
{
    return mkdir("/data/test");
}

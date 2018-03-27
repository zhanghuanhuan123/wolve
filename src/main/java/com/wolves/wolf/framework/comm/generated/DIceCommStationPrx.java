// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.6.3
//
// <auto-generated>
//
// Generated from file `call_interface.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.wolves.wolf.framework.comm.generated;

public interface DIceCommStationPrx extends Ice.ObjectPrx {
    public byte[] _do(byte[] request);

    public byte[] _do(byte[] request, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_do(byte[] request);

    public Ice.AsyncResult begin_do(byte[] request, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_do(byte[] request, Ice.Callback __cb);

    public Ice.AsyncResult begin_do(byte[] request, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_do(byte[] request, Callback_DIceCommStation_do __cb);

    public Ice.AsyncResult begin_do(byte[] request, java.util.Map<String, String> __ctx, Callback_DIceCommStation_do __cb);

    public Ice.AsyncResult begin_do(byte[] request,
                                    IceInternal.Functional_GenericCallback1<byte[]> __responseCb,
                                    IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb);

    public Ice.AsyncResult begin_do(byte[] request,
                                    IceInternal.Functional_GenericCallback1<byte[]> __responseCb,
                                    IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb,
                                    IceInternal.Functional_BoolCallback __sentCb);

    public Ice.AsyncResult begin_do(byte[] request,
                                    java.util.Map<String, String> __ctx,
                                    IceInternal.Functional_GenericCallback1<byte[]> __responseCb,
                                    IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb);

    public Ice.AsyncResult begin_do(byte[] request,
                                    java.util.Map<String, String> __ctx,
                                    IceInternal.Functional_GenericCallback1<byte[]> __responseCb,
                                    IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb,
                                    IceInternal.Functional_BoolCallback __sentCb);

    public byte[] end_do(Ice.AsyncResult __result);

    public void touch(byte[] request);

    public void touch(byte[] request, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_touch(byte[] request);

    public Ice.AsyncResult begin_touch(byte[] request, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_touch(byte[] request, Ice.Callback __cb);

    public Ice.AsyncResult begin_touch(byte[] request, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_touch(byte[] request, Callback_DIceCommStation_touch __cb);

    public Ice.AsyncResult begin_touch(byte[] request, java.util.Map<String, String> __ctx, Callback_DIceCommStation_touch __cb);

    public Ice.AsyncResult begin_touch(byte[] request,
                                       IceInternal.Functional_VoidCallback __responseCb,
                                       IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb);

    public Ice.AsyncResult begin_touch(byte[] request,
                                       IceInternal.Functional_VoidCallback __responseCb,
                                       IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb,
                                       IceInternal.Functional_BoolCallback __sentCb);

    public Ice.AsyncResult begin_touch(byte[] request,
                                       java.util.Map<String, String> __ctx,
                                       IceInternal.Functional_VoidCallback __responseCb,
                                       IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb);

    public Ice.AsyncResult begin_touch(byte[] request,
                                       java.util.Map<String, String> __ctx,
                                       IceInternal.Functional_VoidCallback __responseCb,
                                       IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb,
                                       IceInternal.Functional_BoolCallback __sentCb);

    public void end_touch(Ice.AsyncResult __result);
}
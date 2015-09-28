package com.quanliren.quan_two.service;
interface IQuanPushService {
    void sendMessage(String str);
    boolean getServerSocket();
    void closeAll();
}

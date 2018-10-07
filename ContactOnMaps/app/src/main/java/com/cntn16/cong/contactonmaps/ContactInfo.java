package com.cntn16.cong.contactonmaps;

import com.google.android.gms.maps.model.BitmapDescriptor;

class ContactInfo {
    protected final String name;
    protected final String phone;
    protected final String link;
    protected final double longitude;
    protected final double latitude;
    protected final BitmapDescriptor avatar;

    public ContactInfo(String ten, double kinhDo, double viDo, String sdt, String linkFb, BitmapDescriptor bitmap) {
        name=ten;
        latitude=kinhDo;
        longitude=viDo;
        phone=sdt;
        link=linkFb;
        avatar=bitmap;
    }
}

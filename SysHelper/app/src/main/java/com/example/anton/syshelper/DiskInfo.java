package com.example.anton.syshelper;

public class DiskInfo {
    private String size;
    private String used;
    private String avail;
    private String usedproc;
    private String name;

    public DiskInfo(String size, String used, String avail, String usedproc,String name) {
        this.size = size+"B";
        this.used = used+"B";
        this.avail = avail+"B";
        this.usedproc = usedproc;
        this.name=name;
    }

    public DiskInfo(String info) {
        String res[]=info.split(" ");
        this.name=res[0];
        this.size = res[1]+"B";
        this.used = res[2]+"B";
        this.avail = res[3]+"B";
        this.usedproc = res[4];
    }

    public String getSize() {
        return size;
    }

    public String getUsed() {
        return used;
    }

    public String getAvail() {
        return avail;
    }

    public String getUsedproc() {
        return usedproc;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public void setAvail(String avail) {
        this.avail = avail;
    }

    public void setUsedproc(String usedproc) {
        this.usedproc = usedproc;
    }

    public String getName() {
        return name;
    }
}

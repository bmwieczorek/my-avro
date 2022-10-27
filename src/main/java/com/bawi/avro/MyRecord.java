package com.bawi.avro;

import java.util.List;

public class MyRecord {
    public static class MyRequiredSubRecord {
        private float myRequiredFloat;

        public float getMyRequiredFloat() {
            return myRequiredFloat;
        }

        public void setMyRequiredFloat(float myRequiredFloat) {
            this.myRequiredFloat = myRequiredFloat;
        }
    }

    public static class MyOptionalArraySubRecordType {
        private boolean myRequiredBoolean;

        public boolean isMyRequiredBoolean() {
            return myRequiredBoolean;
        }

        public void setMyRequiredBoolean(boolean myRequiredBoolean) {
            this.myRequiredBoolean = myRequiredBoolean;
        }
    }


    private int myRequiredInt;

    private double myOptionalDouble;

    private String myNullableString;

    private List<Long> myRequiredArrayLongs;

    private MyRequiredSubRecord myRequiredSubRecord;

    private List<MyOptionalArraySubRecordType> myOptionalArraySubRecords;

    public int getMyRequiredInt() {
        return myRequiredInt;
    }

    public void setMyRequiredInt(int myRequiredInt) {
        this.myRequiredInt = myRequiredInt;
    }

    public double getMyOptionalDouble() {
        return myOptionalDouble;
    }

    public void setMyOptionalDouble(double myOptionalDouble) {
        this.myOptionalDouble = myOptionalDouble;
    }

    public String getMyNullableString() {
        return myNullableString;
    }

    public void setMyNullableString(String myNullableString) {
        this.myNullableString = myNullableString;
    }

    public List<Long> getMyRequiredArrayLongs() {
        return myRequiredArrayLongs;
    }

    public void setMyRequiredArrayLongs(List<Long> myRequiredArrayLongs) {
        this.myRequiredArrayLongs = myRequiredArrayLongs;
    }

    public MyRequiredSubRecord getMyRequiredSubRecord() {
        return myRequiredSubRecord;
    }

    public void setMyRequiredSubRecord(MyRequiredSubRecord myRequiredSubRecord) {
        this.myRequiredSubRecord = myRequiredSubRecord;
    }

    public List<MyOptionalArraySubRecordType> getMyOptionalArraySubRecords() {
        return myOptionalArraySubRecords;
    }

    public void setMyOptionalArraySubRecords(List<MyOptionalArraySubRecordType> myOptionalArraySubRecords) {
        this.myOptionalArraySubRecords = myOptionalArraySubRecords;
    }

    @Override
    public String toString() {
        return "MyRecord{" +
                "myRequiredInt=" + myRequiredInt +
                ", myOptionalDouble=" + myOptionalDouble +
                ", myNullableString='" + myNullableString + '\'' +
                ", myRequiredArrayLongs=" + myRequiredArrayLongs +
                ", myRequiredSubRecord=" + myRequiredSubRecord +
                ", myOptionalArraySubRecords=" + myOptionalArraySubRecords +
                '}';
    }
}

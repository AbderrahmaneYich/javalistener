package app.tools.trackingDebugger.helpers;

import java.util.ArrayList;

public class ErrCodeList {
    public static final String noErr = "noErr";
    public static final String inBlackZone = "inBlackZone";
    public static final String externalPowerORSignalNull = "externalPowerORSignalNull";
    public static final String camStar = "camStar";
    public static final String trackingExistInDatabase = "trackingExistInDatabase";
    public static final String isSuspectTime = "isSuspectTime";
    public static final String trackingIsOld = "trackingIsOld";
    public static final String SQLErr = "SQLErr";
    public static final String trackingWithBrokenImportantData = "trackingWithBrokenImportantData";

    public static class ErrList {
        public int noErr = 0;
        public int inBlackZone = 0;
        public int externalPowerORSignalNull = 0;
        public int camStar = 0;
        public int trackingExistInDatabase = 0;
        public int isSuspectTime = 0;
        public int trackingIsOld = 0;
        public int trackingWithBrokenImportantData = 0;
        public int SQLErr = 0;

        public boolean hasErr() {
            return inBlackZone + externalPowerORSignalNull + camStar + trackingExistInDatabase + isSuspectTime + trackingIsOld + trackingWithBrokenImportantData + SQLErr > 0;
        }

        public void addErr(ArrayList<String> errCodes) {
            errCodes.forEach(errCode -> {
                switch (errCode) {
                    case ErrCodeList.noErr:
                        noErr++;
                        break;
                    case ErrCodeList.inBlackZone:
                        inBlackZone++;
                        break;
                    case ErrCodeList.externalPowerORSignalNull:
                        externalPowerORSignalNull++;
                        break;
                    case ErrCodeList.camStar:
                        camStar++;
                        break;
                    case ErrCodeList.trackingExistInDatabase:
                        trackingExistInDatabase++;
                        break;
                    case ErrCodeList.isSuspectTime:
                        isSuspectTime++;
                        break;
                    case ErrCodeList.trackingIsOld:
                        trackingIsOld++;
                        break;
                    case ErrCodeList.trackingWithBrokenImportantData:
                        trackingWithBrokenImportantData++;
                        break;
                    case ErrCodeList.SQLErr:
                        SQLErr++;
                        break;
                }
            });


        }

        @Override
        public String toString() {
            String s = "";
            if (inBlackZone > 0)
                s += "[inBlackZone=" + inBlackZone + "]";
            if (externalPowerORSignalNull > 0)
                s += "[externalPowerORSignalNull=" + externalPowerORSignalNull + "]";
            if (camStar > 0)
                s += "[camStar=" + camStar + "]";
            if (trackingExistInDatabase > 0)
                s += "[trackingExistInDatabase=" + trackingExistInDatabase + "]";
            if (isSuspectTime > 0)
                s += "[isSuspectTime=" + isSuspectTime + "]";
            if (trackingIsOld > 0)
                s += "[trackingIsOld=" + trackingIsOld + "]";
            if (trackingWithBrokenImportantData > 0)
                s += "[trackingWithBrokenImportantData=" + trackingWithBrokenImportantData + "]";
            if (SQLErr > 0)
                s += "[SQLErr=" + SQLErr + "]";

            return s;
        }

    }


}

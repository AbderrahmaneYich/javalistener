package app.tools.vondors;


import app.tools.Console;

import java.util.List;

import static java.lang.Math.*;

public class SphericalUtil {

    private SphericalUtil() {
    }


    public static double computeHeading(LatLng from, LatLng to) {
        double fromLat = toRadians(from.getLatitude());
        double fromLng = toRadians(from.getLongitude());
        double toLat = toRadians(to.getLatitude());
        double toLng = toRadians(to.getLongitude());
        double dLng = toLng - fromLng;
        double heading = atan2(
                sin(dLng) * cos(toLat),
                cos(fromLat) * sin(toLat) - sin(fromLat) * cos(toLat) * cos(dLng));
        return MathUtil.wrap(toDegrees(heading), -180, 180);
    }


    public static LatLng computeOffset(LatLng from, double distance, double heading) {
        distance /= MathUtil.EARTH_RADIUS;
        heading = toRadians(heading);
        double fromLat = toRadians(from.getLatitude());
        double fromLng = toRadians(from.getLongitude());
        double cosDistance = cos(distance);
        double sinDistance = sin(distance);
        double sinFromLat = sin(fromLat);
        double cosFromLat = cos(fromLat);
        double sinLat = cosDistance * sinFromLat + sinDistance * cosFromLat * cos(heading);
        double dLng = atan2(
                sinDistance * cosFromLat * sin(heading),
                cosDistance - sinFromLat * sinLat);
        return new LatLng(toDegrees(asin(sinLat)), toDegrees(fromLng + dLng));
    }


    public static LatLng computeOffsetOrigin(LatLng to, double distance, double heading) {
        heading = toRadians(heading);
        distance /= MathUtil.EARTH_RADIUS;
        double n1 = cos(distance);
        double n2 = sin(distance) * cos(heading);
        double n3 = sin(distance) * sin(heading);
        double n4 = sin(toRadians(to.getLatitude()));

        double n12 = n1 * n1;
        double discriminant = n2 * n2 * n12 + n12 * n12 - n12 * n4 * n4;
        if (discriminant < 0) {
            return null;
        }
        double b = n2 * n4 + sqrt(discriminant);
        b /= n1 * n1 + n2 * n2;
        double a = (n4 - n2 * b) / n1;
        double fromLatRadians = atan2(a, b);
        if (fromLatRadians < -PI / 2 || fromLatRadians > PI / 2) {
            b = n2 * n4 - sqrt(discriminant);
            b /= n1 * n1 + n2 * n2;
            fromLatRadians = atan2(a, b);
        }
        if (fromLatRadians < -PI / 2 || fromLatRadians > PI / 2) {
            return null;
        }
        double fromLngRadians = toRadians(to.getLongitude()) -
                atan2(n3, n1 * cos(fromLatRadians) - n2 * sin(fromLatRadians));
        return new LatLng(toDegrees(fromLatRadians), toDegrees(fromLngRadians));
    }


    public static LatLng interpolate(LatLng from, LatLng to, double fraction) {

        double fromLat = toRadians(from.getLatitude());
        double fromLng = toRadians(from.getLongitude());
        double toLat = toRadians(to.getLatitude());
        double toLng = toRadians(to.getLongitude());
        double cosFromLat = cos(fromLat);
        double cosToLat = cos(toLat);

        double angle = computeAngleBetween(from, to);
        double sinAngle = sin(angle);
        if (sinAngle < 1E-6) {
            return from;
        }
        double a = sin((1 - fraction) * angle) / sinAngle;
        double b = sin(fraction * angle) / sinAngle;

        double x = a * cosFromLat * cos(fromLng) + b * cosToLat * cos(toLng);
        double y = a * cosFromLat * sin(fromLng) + b * cosToLat * sin(toLng);
        double z = a * sin(fromLat) + b * sin(toLat);

        double lat = atan2(z, sqrt(x * x + y * y));
        double lng = atan2(y, x);
        return new LatLng(toDegrees(lat), toDegrees(lng));
    }


    private static double distanceRadians(double lat1, double lng1, double lat2, double lng2) {
        return MathUtil.arcHav(MathUtil.havDistance(lat1, lat2, lng1 - lng2));
    }


    static double computeAngleBetween(LatLng from, LatLng to) {
        return distanceRadians(toRadians(from.getLatitude()), toRadians(from.getLongitude()),
                toRadians(to.getLatitude()), toRadians(to.getLongitude()));
    }


    public static double computeDistanceBetween(LatLng from, LatLng to) {
        try {
            return computeAngleBetween(from, to) * MathUtil.EARTH_RADIUS;
        } catch (Exception e) {
            Console.printStackTrace(e);
            return 0;
        }
    }


    public static double computeLength(List<LatLng> path) {
        if (path.size() < 2) {
            return 0;
        }
        double length = 0;
        LatLng prev = path.get(0);
        double prevLat = toRadians(prev.getLatitude());
        double prevLng = toRadians(prev.getLongitude());
        for (LatLng point : path) {
            double lat = toRadians(point.getLatitude());
            double lng = toRadians(point.getLongitude());
            length += distanceRadians(prevLat, prevLng, lat, lng);
            prevLat = lat;
            prevLng = lng;
        }
        return length * MathUtil.EARTH_RADIUS;
    }


    public static double computeLength(LatLng[] path) {
        if (path.length < 2) {
            return 0;
        }
        double length = 0;
        LatLng prev = path[0];
        double prevLat = toRadians(prev.getLatitude());
        double prevLng = toRadians(prev.getLongitude());
        for (LatLng point : path) {
            double lat = toRadians(point.getLatitude());
            double lng = toRadians(point.getLongitude());
            length += distanceRadians(prevLat, prevLng, lat, lng);
            prevLat = lat;
            prevLng = lng;
        }
        return length * MathUtil.EARTH_RADIUS;
    }


    public static double computeArea(List<LatLng> path) {
        return abs(computeSignedArea(path));
    }


    public static double computeSignedArea(List<LatLng> path) {
        return computeSignedArea(path, MathUtil.EARTH_RADIUS);
    }


    static double computeSignedArea(List<LatLng> path, double radius) {
        int size = path.size();
        if (size < 3) {
            return 0;
        }
        double total = 0;
        LatLng prev = path.get(size - 1);
        double prevTanLat = tan((PI / 2 - toRadians(prev.getLatitude())) / 2);
        double prevLng = toRadians(prev.getLongitude());
        for (LatLng point : path) {
            double tanLat = tan((PI / 2 - toRadians(point.getLatitude())) / 2);
            double lng = toRadians(point.getLongitude());
            total += polarTriangleArea(tanLat, lng, prevTanLat, prevLng);
            prevTanLat = tanLat;
            prevLng = lng;
        }
        return total * (radius * radius);
    }


    private static double polarTriangleArea(double tan1, double lng1, double tan2, double lng2) {
        double deltaLng = lng1 - lng2;
        double t = tan1 * tan2;
        return 2 * atan2(t * sin(deltaLng), 1 + t * cos(deltaLng));
    }
}
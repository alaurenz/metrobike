package com.HuskySoft.metrobike.backend.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.HuskySoft.metrobike.backend.Leg;
import com.HuskySoft.metrobike.backend.Location;
import com.HuskySoft.metrobike.backend.Route;
import com.HuskySoft.metrobike.backend.WebRequestJSONKeys;

/**
 * This class is responsible for testing the route class.
 * 
 * @author coreyh3
 * 
 *         check style: Sam Wilson
 */
public class RouteTest extends TestCase {
    /**
     * The duration in second.
     */
    private static final int DUR_IN_SEC = 761;

    /**
     * The distance in meter.
     */
    private static final int DIST_IN_METER = 3402;

    /**
     * The 2nd longitude.
     */
    private static final double LNG_2 = -122.32582;

    /**
     * The 2nd Latitude.
     */
    private static final double LAT_2 = 47.65358000000001;

    /**
     * The 1st longitude.
     */
    private static final double LNG_1 = -122.31325;

    /**
     * The 1st Latitude.
     */
    private static final double LAT_1 = 47.67604;

    /**
     * This is a route variable that is used for testing this class.
     */
    private Route route = null;

    /**
     * This initializes the route variable for each test.
     * 
     * @throws JSONException
     *             the json exception
     * 
     * @throws Exception
     */
    // @Before
    public final void setUp() throws JSONException {
        JSONObject myJSON = new JSONObject(dummyBicycleJSON);
        JSONArray routesArray = myJSON.getJSONArray(WebRequestJSONKeys.ROUTES.getLowerCase());
        route = Route.buildRouteFromJSON(routesArray.getJSONObject(0));
    }

    /**
     * WhiteBox: This tests the setSummary method.
     * 
     * @throws JSONException
     *             the json exception
     */
    // @Test
    public final void testSetSummaryTest() throws JSONException {
        setUp();
        String summary = "This is a summary";
        String expected = summary;
        route.setSummary(summary);
        String actual = route.getSummary();
        Assert.assertEquals("Actual value for route.getSummary was: " + actual, expected, actual);
    }

    /**
     * WhiteBox: This tests the getWarnings method.
     * 
     * @throws JSONException
     *             the json exception
     */
    // @Test
    public final void testGetWarningsTest() throws JSONException {
        setUp();
        String expected =
                "Bicycling directions are in beta. Use caution - "
                        + "This route may contain streets that aren't suited for bicycling.";
        List<String> warnings = route.getWarnings();
        String actual = "";
        for (int i = 0; i < warnings.size(); i++) {
            actual += warnings.get(i);
        }
        System.err.println("Expected:\n" + expected + "\n");
        System.err.println("Actual:\n" + actual + "\n");

        Assert.assertEquals("Actual value for route.getWarnings() (Note:concatenated "
                + "the list together) was: " + actual, expected, actual);
    }

    /**
     * WhiteBox: This tests the getNeBound method.
     * 
     * @throws JSONException
     *             the json exception
     */
    // @Test
    public final void testGetNEBoundTest() throws JSONException {
        setUp();
        Location expected = new Location(LAT_1, LNG_1);
        Location actual = route.getNeBound();
        Assert.assertEquals("Actual value for route.getNeBound() was: " + actual.toString(),
                expected, actual);
    }

    /**
     * WhiteBox: This tests the get SwBound method.
     * 
     * @throws JSONException
     *             the json exception
     */
    // @Test
    public final void testGetSWBoundTest() throws JSONException {
        setUp();
        Location expected = new Location(LAT_2, LNG_2);
        Location actual = route.getSwBound();
        Assert.assertEquals("Actual value for route.getSwBound() was: " + actual.toString(),
                expected, actual);
    }

    /**
     * WhiteBox: This tests the getDistanceInMeters() method.
     * 
     * @throws JSONException
     *             the json exception
     */
    // @Test
    public final void testGetdistanceInMetersTest() throws JSONException {
        setUp();
        long expected = DIST_IN_METER;
        long actual = route.getDistanceInMeters();
        Assert.assertEquals("Actual value for route.getDistanceInMeters() was: " + actual,
                expected, actual);
    }

    /**
     * WhiteBox: This tests the getDurationInSeconds method.
     * 
     * @throws JSONException
     *             the json exception
     */
    // @Test
    public final void testGetDurationInSecondsTest() throws JSONException {
        setUp();
        long expected = DUR_IN_SEC;
        long actual = route.getDurationInSeconds();
        Assert.assertEquals("Actual value for route.getDurationInSeconds() was: " + actual,
                expected, actual);
    }

    /**
     * WhiteBox: This tests the getLegList method and verifies that the returned
     * list is not null.
     * 
     * @throws JSONException
     *             the json exception
     */
    // @Test
    public final void testGetLegListListIsNotNullTest() throws JSONException {
        setUp();
        List<Leg> expected = null;
        List<Leg> actual = route.getLegList();
        Assert.assertFalse("Actual value for route.getLegList() was: " + actual,
                actual.equals(expected));
    }

    /**
     * WhiteBox: This tests the getLegList method and verifies that the returned
     * list is not size zero.
     * 
     * @throws JSONException
     *             the json exception
     */
    // @Test
    public final void testGetLegListListIsNotSizeZeroTest() throws JSONException {
        setUp();
        int expected = 0;
        int actual = route.getLegList().size();
        Assert.assertFalse("Actual value for route.getLegList().size() was: " + actual,
                expected == actual);
    }

    /**
     * WhiteBox: This tests the getDirectionsStepsText method.
     * 
     * @throws JSONException
     *             the json exception
     */
    // @Test
    public final void testGetDirectionsStepsTextTest() throws JSONException {
        setUp();
        String expected =
                "Head <b>southeast</b> on <b>Latona Ave NE</b> toward <b>NE 65th St</b>"
                        + "Turn <b>left</b> onto <b>NE 65th St</b>Turn <b>right</b> onto "
                        + "<b>NE Ravenna Blvd</b>Turn <b>right</b> onto <b>"
                        + "Roosevelt Way NE</b>Turn "
                        + "<b>left</b> onto <b>NE Campus Pkwy</b>Turn <b>right</b> "
                        + "onto <b>University "
                        + "Way NE</b>Turn <b>right</b> onto <b>Burke-Gilman Trail</b>"
                        + "Turn <b>left</b> "
                        + "onto <b>Brooklyn Ave NE</b><div style=\"font-size:0.9em\">"
                        + "Destination will be on the right</div>";
        List<String> text = route.directionStepsText();
        String actual = "";
        for (int i = 0; i < text.size(); i++) {
            actual += text.get(i);
        }

        Assert.assertEquals("Actual value for route.directionsStepsText() "
                + "(concatenated the list for testing) was: " + actual, expected, actual);
    }

    /**
     * WhiteBox: This tests the getPolyLinePoints method and checks that it is
     * not null.
     * 
     * @throws JSONException
     *             the json exception
     */
    // @Test
    public final void testGetPolyLinePointsNotNullTest() throws JSONException {
        setUp();
        List<Location> expected = null;
        List<Location> actual = route.getPolyLinePoints();
        Assert.assertFalse("Actual value for route.getPolyLinePoints() was: " + actual,
                actual.equals(expected));
    }

    /**
     * WhiteBox: This tests the getPolyLinePoints method and checks that it is
     * not size zero.
     * 
     * @throws JSONException
     *             the json exception
     */
    // @Test
    public final void testGetPolyLinePointsNotSizeZeroTest() throws JSONException {
        setUp();
        int expected = 0;
        int actual = route.getPolyLinePoints().size();
        Assert.assertFalse("Actual value for route.getPolyLinePoints().size() was: " + actual,
                expected == actual);
    }

    /**
     * WhiteBox: This tests the toString method.
     * 
     * @throws JSONException
     *             the json exception
     */
    // @Test
    public final void testToStringTest() throws JSONException {
        setUp();
        String expected = dummyBicycleJSONToString;
        String actual = route.toString();
        Assert.assertEquals("Actual value for route.toString() was: " + actual, expected, actual);
    }

    /**
     * BlackBox: Tests to be sure we can safely serialize and deserialize a
     * Rotue object. This functionality is used in the intent-passing system.
     * 
     * @throws IOException
     *             if an IO exception occurs during processing
     * @throws ClassNotFoundException
     *             if a class cannot be found
     */
    public final void testSerializationTestEmptyRouteObject() throws IOException,
            ClassNotFoundException {
        Route testRoute = new Route();

        // Serialize the empty request, then de-serialize it
        byte[] theBytes = helpSerialize(testRoute);
        Route recreatedRoute = helpDeserialize(theBytes);

        // Use string equality to check the request
        Assert.assertEquals("The toString() representation of a serialized->deserialized"
                + " object should remain unchanged.", testRoute.toString(),
                recreatedRoute.toString());
    }

    /**
     * BlackBox: Tests to be sure we can safely serialize and deserialize a
     * non-empty Route object. This functionality is used in the intent-passing
     * system.
     * 
     * @throws IOException
     *             if an IO exception occurs during processing
     * @throws ClassNotFoundException
     *             if a class cannot be found
     * @throws JSONException
     *             the json exception.
     */
    public final void testSerializationTestNonEmptyRouteObject() throws IOException,
            ClassNotFoundException,
            JSONException {
        setUp();

        // Serialize the request, then de-serialize it
        byte[] theBytes = helpSerialize(route);
        Route recreatedRoute = helpDeserialize(theBytes);

        // Use string equality to check the request
        Assert.assertEquals("The toString() representation of a serialized->deserialized"
                + " object should remain unchanged.", route.toString(), recreatedRoute.toString());
    }

    /**
     * Helper function for serializing a Route object.Help on testing this based
     * on. http://www.ibm.com/developerworks/library/j-serialtest/index.html
     * 
     * @param toSerialize
     *            the Route to serialize
     * @return a byte array representing the serialized Route
     * @throws IOException
     *             if an IO exception occurs during processing
     */
    private byte[] helpSerialize(final Route toSerialize) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
        objectOut.writeObject(toSerialize);
        objectOut.close();
        return byteOut.toByteArray();
    }

    /**
     * Helper function for serializing a Route object.Help on testing this based
     * on. http://www.ibm.com/developerworks/library/j-serialtest/index.html
     * 
     * @param toDeSerialize
     *            a byte array representing the serialized Route
     * @return the deserialized Route
     * @throws IOException
     *             if an IO exception occurs during processing
     * @throws ClassNotFoundException
     *             if a class cannot be found
     */
    private Route helpDeserialize(final byte[] toDeSerialize) throws ClassNotFoundException,
            IOException {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(toDeSerialize);
        ObjectInputStream objectIn = new ObjectInputStream(byteIn);
        return (Route) objectIn.readObject();
    }

    /**
     * This is the value to compare the toString test to.
     */
    private final String dummyBicycleJSONToString = "Route:\n   " + " neBound: Location:\n    "
            + "Latitude: 47.67604\n    Longitude: -122.31325\n\n    swBound: Location:\n    "
            + "Latitude: 47.65358000000001\n    Longitude: -122.32582\n\n    "
            + "summary: no summary\n    Warnings:\n        "
            + "0: Bicycling directions are in beta. Use caution - This route may contain streets "
            + "that aren't suited for bicycling.\n    legList:\n    Leg:\n            "
            + "startAddress: 6504 Latona Avenue Northeast, Seattle, WA 98115, USA\n            "
            + "endAddress: 3801 Brooklyn Avenue Northeast, University of Washington, Seattle, "
            + "WA 98105, USA\n            stepList:\n    Step\n        distanceInMeters: "
            + "18\n        durationInSeconds: 6\n        startLocation:     Location:\n        "
            + "Latitude: 47.67604\n        Longitude: -122.32582\n\n        endLocation:     "
            + "Location:\n        Latitude: 47.67591\n        Longitude: -122.32569\n\n        "
            + "travelMode: BICYCLING\n        htmlInstruction: Head <b>southeast</b> on <b>Latona "
            + "Ave NE</b> toward <b>NE 65th St</b>\n        polyLine: gv~aHjwriVXY\n        "
            + "substepList:\n    Step\n        distanceInMeters: 252\n        durationInSeconds: "
            + "63\n        startLocation:     Location:\n        Latitude: 47.67591\n        "
            + "Longitude: -122.32569\n\n        endLocation:     Location:\n        "
            + "Latitude: 47.67588000000001\n        Longitude: -122.32233\n\n        "
            + "travelMode: BICYCLING\n        htmlInstruction: Turn <b>left</b> onto <b>NE "
            + "65th St</b>\n        polyLine: mu~aHpvriV@s@@qE@gE?w@?u@?cB\n        "
            + "substepList:\n    Step\n        distanceInMeters: 593\n        durationInSeconds: "
            + "142\n        startLocation:     Location:\n"
            + "        Latitude: 47.67588000000001\n        "
            + "Longitude: -122.32233\n\n        endLocation:     Location:\n        Latitude: "
            + "47.67201000000001\n        Longitude: -122.31736\n\n        "
            + "travelMode: BICYCLING\n        htmlInstruction: Turn <b>right</b> onto <b>NE "
            + "Ravenna Blvd</b>\n        "
            + "polyLine: gu~aHpariVZYn@m@bA{@x@s@POFI`@]XWBCBEZYNOh@e@dB{ALMPOjC}BZYN]Jq@|@}G\n"
            + "        substepList:\n    Step\n        distanceInMeters: 1775\n        "
            + "durationInSeconds: 378\n        startLocation:     Location:\n        "
            + "Latitude: 47.67201000000001\n        Longitude: -122.31736\n\n        "
            + "endLocation:     "
            + "Location:\n        Latitude: 47.65609000000001\n        Longitude: "
            + "-122.31788\n\n        travelMode: BICYCLING\n        htmlInstruction: "
            + "Turn <b>right</b> onto <b>Roosevelt Way NE</b>\n        "
            + "polyLine: a}}aHnbqiVnA?X?nC?jC?V?f@?|@?VApC@fA@~E@~B?hA@P?r@?xB@b@?^?`"
            + "A@R?jCAjB@~GDj@F^PRLHDHBFBJ@L?tC?~@BpA@r@@`DBlD@|BB\\?^@r@?L@@?PDHB\n        "
            + "substepList:\n    Step\n        distanceInMeters: 348\n        durationInSeconds: "
            + "114\n        startLocation:     Location:\n        Latitude: 47.65609000000001\n"
            + "        Longitude: -122.31788\n\n        endLocation:     Location:\n        "
            + "Latitude: 47.65598000000001\n        Longitude: -122.31325\n\n        travelMode: "
            + "BICYCLING\n        htmlInstruction: Turn <b>left</b> onto <b>NE Campus Pkwy</b>\n"
            + "        polyLine: qyzaHveqiVDS@I@IAkAByB?OBkE@sB?C?qB@wB@qB\n        substepList:\n"
            + "    Step\n        distanceInMeters: 268\n        durationInSeconds:"
            + " 31\n        "
            + "startLocation:     Location:\n        Latitude: 47.65598000000001\n"
            + "        Longitude: " + "-122.31325\n\n        endLocation:"
            + "     Location:\n        " + "Latitude: 47.65358000000001\n"
            + "        Longitude: -122.31334\n\n        travelMode: BICYCLING\n"
            + "        htmlInstruction:" + " Turn <b>right</b> onto <b>University Way NE</b>\n"
            + "        "
            + "polyLine: {xzaHxhpiV^B|A@hCDlB@L@zA@\n        substepList:\n    Step\n        "
            + "distanceInMeters: 113\n        durationInSeconds: 21\n        startLocation:     "
            + "Location:\n        Latitude: 47.65358000000001\n        "
            + "Longitude: -122.31334\n\n        "
            + "endLocation:     Location:\n        Latitude: 47.65422\n        "
            + "Longitude: -122.31447\n\n        travelMode: BICYCLING\n        "
            + "htmlInstruction: Turn <b>right</b> onto <b>Burke-Gilman" + " Trail</b>\n        "
            + "polyLine: {izaHjipiVM`@Sh@EHCDEBIDGDEDINEJOb@Un@\n        substepList:\n    "
            + "Step\n        distanceInMeters: 35\n        durationIn" + "Seconds: 6\n        "
            + "startLocation:     Location:\n        Latitude: 47.65" + "422\n        Longitude: "
            + "-122.31447\n\n        endLocation:     Location:\n        "
            + "Latitude: 47.65390000000001\n"
            + "        Longitude: -122.31448\n\n        travelMode: BICYCLING\n"
            + "        htmlInstruction:" + " Turn <b>left</b> onto <b>Brooklyn Ave NE</b><div"
            + " style=\"font-size" + ":0.9em\">Destination"
            + " will be on the right</div>\n        polyLine: {mzaHlppiVp@@L?\n"
            + "        substepList:\n";

    /**
     * Dummy JSON bicycle directions.
     */
    private final String dummyBicycleJSON = "{" + "\n   \"routes\" : [\n    "
            + "  {\n         \"bounds\" : {\n            \"northeast\" : {\n  "
            + "             \"lat\" : 47.676040,\n               \"lng\" : -12"
            + "2.313250\n            },\n            \"southwest\" : {\n      "
            + "         \"lat\" : 47.65358000000001,\n               \"lng\" :"
            + " -122.325820\n            }\n         },\n         \"copyrights"
            + "\" : \"Map data \u00a92013 Google\",\n         \"legs\" : [\n  "
            + "          {\n               \"distance\" : {\n                 "
            + " \"text\" : \"2.1 mi\",\n                  \"value\" : 3402\n  "
            + "             },\n               \"duration\" : {\n             "
            + "     \"text\" : \"13 mins\",\n                  \"value\" : 761"
            + "\n               },\n               \"end_address\" : \"3801 Br"
            + "ooklyn Avenue Northeast, University of Washington, Seattle, WA "
            + "98105, USA\",\n               \"end_location\" : {\n           "
            + "       \"lat\" : 47.65390000000001,\n                  \"lng\" "
            + ": -122.314480\n               },\n               \"start_addres"
            + "s\" : \"6504 Latona Avenue Northeast, Seattle, WA 98115, USA\","
            + "\n               \"start_location\" : {\n                  \"la"
            + "t\" : 47.676040,\n                  \"lng\" : -122.325820\n    "
            + "           },\n               \"steps\" : [\n                  "
            + "{\n                     \"distance\" : {\n                     "
            + "   \"text\" : \"59 ft\",\n                        \"value\" : 1"
            + "8\n                     },\n                     \"duration\" :"
            + " {\n                        \"text\" : \"1 min\",\n            "
            + "            \"value\" : 6\n                     },\n           "
            + "          \"end_location\" : {\n                        \"lat\""
            + " : 47.675910,\n                        \"lng\" : -122.325690\n "
            + "                    },\n                     \"html_instruction"
            + "s\" : \"Head \\u003cb\\u003esoutheast\\u003c/b\\u003e on \\u003"
            + "cb\\u003eLatona Ave NE\\u003c/b\\u003e toward \\u003cb\\u003eNE"
            + " 65th St\\u003c/b\\u003e\",\n                     \"polyline\" "
            + ": {\n                        \"points\" : \"gv~aHjwriVXY\"\n   "
            + "                  },\n                     \"start_location\" :"
            + " {\n                        \"lat\" : 47.676040,\n             "
            + "           \"lng\" : -122.325820\n                     },\n    "
            + "                 \"travel_mode\" : \"BICYCLING\"\n             "
            + "     },\n                  {\n                     \"distance\""
            + " : {\n                        \"text\" : \"0.2 mi\",\n         "
            + "               \"value\" : 252\n                     },\n      "
            + "               \"duration\" : {\n                        \"text"
            + "\" : \"1 min\",\n                        \"value\" : 63\n      "
            + "               },\n                     \"end_location\" : {\n "
            + "                       \"lat\" : 47.67588000000001,\n          "
            + "              \"lng\" : -122.322330\n                     },\n "
            + "                    \"html_instructions\" : \"Turn \\u003cb\\u0"
            + "03eleft\\u003c/b\\u003e onto \\u003cb\\u003eNE 65th St\\u003c/b"
            + "\\u003e\",\n                     \"polyline\" : {\n            "
            + "            \"points\" : \"mu~aHpvriV@s@@qE@gE?w@?u@?cB\"\n    "
            + "                 },\n                     \"start_location\" : "
            + "{\n                        \"lat\" : 47.675910,\n              "
            + "          \"lng\" : -122.325690\n                     },\n     "
            + "                \"travel_mode\" : \"BICYCLING\"\n              "
            + "    },\n                  {\n                     \"distance\" "
            + ": {\n                        \"text\" : \"0.4 mi\",\n          "
            + "              \"value\" : 593\n                     },\n       "
            + "              \"duration\" : {\n                        \"text"
            + "\" : \"2 mins\",\n                        \"value\" : 142\n    "
            + "                 },\n                     \"end_location\" : {"
            + "\n                        \"lat\" : 47.67201000000001,\n       "
            + "                 \"lng\" : -122.317360\n                     },"
            + "\n                     \"html_instructions\" : \"Turn \\u003cb"
            + "\\u003eright\\u003c/b\\u003e onto \\u003cb\\u003eNE Ravenna Blv"
            + "d\\u003c/b\\u003e\",\n                     \"polyline\" : {\n  "
            + "                      \"points\" : \"gu~aHpariVZYn@m@bA{@x@s@PO"
            + "FI`@]XWBCBEZYNOh@e@dB{ALMPOjC}BZYN]Jq@|@}G\"\n                 "
            + "    },\n                     \"start_location\" : {\n          "
            + "              \"lat\" : 47.67588000000001,\n                   "
            + "     \"lng\" : -122.322330\n                     },\n          "
            + "           \"travel_mode\" : \"BICYCLING\"\n                  }"
            + ",\n                  {\n                     \"distance\" : {\n"
            + "                        \"text\" : \"1.1 mi\",\n               "
            + "         \"value\" : 1775\n                     },\n           "
            + "          \"duration\" : {\n                        \"text\" : "
            + "\"6 mins\",\n                        \"value\" : 378\n         "
            + "            },\n                     \"end_location\" : {\n    "
            + "                    \"lat\" : 47.65609000000001,\n             "
            + "           \"lng\" : -122.317880\n                     },\n    "
            + "                 \"html_instructions\" : \"Turn \\u003cb\\u003e"
            + "right\\u003c/b\\u003e onto \\u003cb\\u003eRoosevelt Way NE\\u00"
            + "3c/b\\u003e\",\n                     \"polyline\" : {\n        "
            + "                \"points\" : \"a}}aHnbqiVnA?X?nC?jC?V?f@?|@?VAp"
            + "C@fA@~E@~B?hA@P?r@?xB@b@?^?`A@R?jCAjB@~GDj@F^PRLHDHBFBJ@L?tC?~@"
            + "BpA@r@@`DBlD@|BB\\\\?^@r@?L@@?PDHB\"\n                     },\n"
            + "                     \"start_location\" : {\n                  "
            + "      \"lat\" : 47.67201000000001,\n                        \"l"
            + "ng\" : -122.317360\n                     },\n                  "
            + "   \"travel_mode\" : \"BICYCLING\"\n                  },\n     "
            + "             {\n                     \"distance\" : {\n        "
            + "                \"text\" : \"0.2 mi\",\n                       "
            + " \"value\" : 348\n                     },\n                    "
            + " \"duration\" : {\n                        \"text\" : \"2 mins"
            + "\",\n                        \"value\" : 114\n                 "
            + "    },\n                     \"end_location\" : {\n            "
            + "            \"lat\" : 47.65598000000001,\n                     "
            + "   \"lng\" : -122.313250\n                     },\n            "
            + "         \"html_instructions\" : \"Turn \\u003cb\\u003eleft\\u0"
            + "03c/b\\u003e onto \\u003cb\\u003eNE Campus Pkwy\\u003c/b\\u003e"
            + "\",\n                     \"polyline\" : {\n                   "
            + "     \"points\" : \"qyzaHveqiVDS@I@IAkAByB?OBkE@sB?C?qB@wB@qB\""
            + "\n                     },\n                     \"start_locatio"
            + "n\" : {\n                        \"lat\" : 47.65609000000001,\n"
            + "                        \"lng\" : -122.317880\n                "
            + "     },\n                     \"travel_mode\" : \"BICYCLING\"\n"
            + "                  },\n                  {\n                    "
            + " \"distance\" : {\n                        \"text\" : \"0.2 mi"
            + "\",\n                        \"value\" : 268\n                 "
            + "    },\n                     \"duration\" : {\n                "
            + "        \"text\" : \"1 min\",\n                        \"value"
            + "\" : 31\n                     },\n                     \"end_lo"
            + "cation\" : {\n                        \"lat\" : 47.653580000000"
            + "01,\n                        \"lng\" : -122.313340\n           "
            + "          },\n                     \"html_instructions\" : \"Tu"
            + "rn \\u003cb\\u003eright\\u003c/b\\u003e onto \\u003cb\\u003eUni"
            + "versity Way NE\\u003c/b\\u003e\",\n                     \"polyl"
            + "ine\" : {\n                        \"points\" : \"{xzaHxhpiV^B|"
            + "A@hCDlB@L@zA@\"\n                     },\n                     "
            + "\"start_location\" : {\n                        \"lat\" : 47.65"
            + "598000000001,\n                        \"lng\" : -122.313250\n "
            + "                    },\n                     \"travel_mode\" : "
            + "\"BICYCLING\"\n                  },\n                  {\n     "
            + "                \"distance\" : {\n                        \"tex"
            + "t\" : \"371 ft\",\n                        \"value\" : 113\n   "
            + "                  },\n                     \"duration\" : {\n  "
            + "                      \"text\" : \"1 min\",\n                  "
            + "      \"value\" : 21\n                     },\n                "
            + "     \"end_location\" : {\n                        \"lat\" : 47"
            + ".654220,\n                        \"lng\" : -122.314470\n      "
            + "               },\n                     \"html_instructions\" :"
            + " \"Turn \\u003cb\\u003eright\\u003c/b\\u003e onto \\u003cb\\u00"
            + "3eBurke-Gilman Trail\\u003c/b\\u003e\",\n                     "
            + "\"polyline\" : {\n                        \"points\" : \"{izaHj"
            + "ipiVM`@Sh@EHCDEBIDGDEDINEJOb@Un@\"\n                     },\n  "
            + "                   \"start_location\" : {\n                    "
            + "    \"lat\" : 47.65358000000001,\n                        \"lng"
            + "\" : -122.313340\n                     },\n                    "
            + " \"travel_mode\" : \"BICYCLING\"\n                  },\n       "
            + "           {\n                     \"distance\" : {\n          "
            + "              \"text\" : \"115 ft\",\n                        "
            + "\"value\" : 35\n                     },\n                     "
            + "\"duration\" : {\n                        \"text\" : \"1 min\","
            + "\n                        \"value\" : 6\n                     }"
            + ",\n                     \"end_location\" : {\n                 "
            + "       \"lat\" : 47.65390000000001,\n                        \""
            + "lng\" : -122.314480\n                     },\n                 "
            + "    \"html_instructions\" : \"Turn \\u003cb\\u003eleft\\u003c/b"
            + "\\u003e onto \\u003cb\\u003eBrooklyn Ave NE\\u003c/b\\u003e\\u0"
            + "03cdiv style=\\\"font-size:0.9em\\\"\\u003eDestination will be "
            + "on the right\\u003c/div\\u003e\",\n                     \"polyl"
            + "ine\" : {\n                        \"points\" : \"{mzaHlppiVp@@"
            + "L?\"\n                     },\n                     \"start_loc"
            + "ation\" : {\n                        \"lat\" : 47.654220,\n    "
            + "                    \"lng\" : -122.314470\n                    "
            + " },\n                     \"travel_mode\" : \"BICYCLING\"\n    "
            + "              }\n               ],\n               \"via_waypoi"
            + "nt\" : []\n            }\n         ],\n         \"overview_poly"
            + "line\" : {\n            \"points\" : \"gv~aHjwriVXY@s@ByK?mB?cB"
            + "ZYrBiBjAcAfAcAxAuApGwFZYN]hAoIdK?tCAxPDbLDvF?~GDj@Fr@^RHRDbD?fJ"
            + "JhJF`A@RDHBDSBS@eEB{EBaJ@qB^BfFFzBBzA@M`@Yr@IHQJOTUn@Un@p@@L?\""
            + "\n         },\n         \"summary\" : \"Roosevelt Way NE\",\n  "
            + "       \"warnings\" : [\n            \"Bicycling directions are"
            + " in beta. Use caution - This route may contain streets tha"
            + "t aren\'t suited for bicycling.\"\n         ],\n         \"wayp"
            + "oint_order\" : []\n      }\n   ],\n   \"status\" : \"OK\"\n}";

}

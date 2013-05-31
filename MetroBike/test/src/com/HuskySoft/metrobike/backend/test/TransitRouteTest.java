package com.HuskySoft.metrobike.backend.test;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.HuskySoft.metrobike.backend.Route;
import com.HuskySoft.metrobike.backend.TransitRoute;
import com.HuskySoft.metrobike.backend.WebRequestJSONKeys;

/**
 * This class is responsible for testing the TransitRoute class.
 * 
 * @author Adrian Laurenzi
 * 
 */
public class TransitRouteTest extends TestCase {
    /**
     * This is a TransitDetails variable that is used for testing this class.
     */
    private TransitRoute transitRoute = null;

    /**
     * This initializes the transitRoute variable for each test.
     * 
     * @throws JSONException
     * 
     * @throws Exception
     */
    // @Before
    public void setUp() throws JSONException {
        JSONObject transitJSON = new JSONObject(dummyTransitJSON);
        JSONArray routesArray = transitJSON.getJSONArray(WebRequestJSONKeys.ROUTES.getLowerCase());
        Route route = Route.buildRouteFromJSON(routesArray.getJSONObject(0));
        transitRoute = new TransitRoute(route);
    }

    /**
     * WhiteBox: This tests the getSourceRoute method.
     * 
     * @throws JSONException
     */
    // @Test
    public void test_getSourceRoute() throws JSONException {
        setUp();
        Assert.assertNotNull(transitRoute.getSourceRoute());
    }
    
    /**
     * WhiteBox: This tests the getDurationInSeconds method.
     * 
     * @throws JSONException
     */
    // @Test
    public void test_getDurationInSeconds() throws JSONException {
        setUp();
        long expected = 659;
        long actual = transitRoute.getDurationInSeconds();
        Assert.assertEquals(
        		"Actual value for transitDetails.getDurationInSeconds() was: "
                + actual, expected, actual);
    }
    
    /**
     * WhiteBox: This tests the getTransitDurationInSeconds method.
     * 
     * @throws JSONException
     */
    // @Test
    public void test_getTransitDurationInSeconds() throws JSONException {
        setUp();
        long expected = 639;
        long actual = transitRoute.getTransitDurationInSeconds();
        Assert.assertEquals(
        		"Actual value for transitDetails.getTransitDurationInSeconds() was: "
                + actual, expected, actual);
    }
    
    /**
     * Dummy JSON transit directions
     */
    private final String dummyTransitJSON = "{   \"routes\" : [    " +
    		"  {         \"bounds\" : {            \"northeast\" : { " +
    		"              \"lat\" : 47.68152000000001,              " +
    		" \"lng\" : -122.311880            },            " +
    		"\"southwest\" : {               \"lat\" : 47.66135000000001," +
    		"               \"lng\" : -122.327360            }         }, " +
    		"        \"copyrights\" : \"Map data ©2013 Google\",        " +
    		" \"legs\" : [            {               \"arrival_time\" : { " +
    		"                 \"text\" : \"13:58\",               " +
    		"   \"time_zone\" : \"America/Los_Angeles\",           " +
    		"       \"value\" : 1368997138               },         " +
    		"      \"departure_time\" : {                 " +
    		" \"text\" : \"13:47\",                  \"time_zone\" : " +
    		"\"America/Los_Angeles\",                  \"value\" :" +
    		" 1368996430               },               \"distance\"" +
    		" : {                  \"text\" : \"2.0 mi\",          " +
    		"        \"value\" : 3142               },              " +
    		" \"duration\" : {                 " +
    		" \"text\" : \"11 mins\",                  \"value\" : 659 " +
    		"              },               " +
    		"\"end_address\" : \"1415 Northeast 45th Street, " +
    		"University of Washington, Seattle, WA 98105, USA\",   " +
    		"           \"end_location\" : {                " +
    		"  \"lat\" : 47.66135000000001,                " +
    		"  \"lng\" : -122.312080               },               " +
    		"\"start_address\" : \"East Green LK Dr N & Latona Ave NE, " +
    		"Seattle, WA 98115, USA\",               \"start_location\" : { " +
    		"                 \"lat\" : 47.681470,                  " +
    		"\"lng\" : -122.327360               },              " +
    		" \"steps\" : [                  {                     " +
    		"\"distance\" : {                        \"text\" : " +
    		"\"1 ft\",                        \"value\" : 0        " +
    		"             },                     \"duration\" : {     " +
    		"                   \"text\" : \"1 min\",                 " +
    		"       \"value\" : 20                     },                " +
    		"     \"end_location\" : {                        \"lat\" :" +
    		" 47.681470,                        \"lng\" : -122.327350    " +
    		"                 },                     \"html_instructions\" " +
    		": \"Walk to East Green LK Dr N & Latona Ave NE\",       " +
    		"              \"polyline\" : {                       " +
    		" \"points\" : \"ex_bH~`siV?A\"                     },     " +
    		"                \"start_location\" : {                    " +
    		"    \"lat\" : 47.681470,                        \"lng\" " +
    		": -122.327360                     },                    " +
    		" \"steps\" : [                        {                  " +
    		"         \"distance\" : {                              " +
    		"\"text\" : \"1 ft\",                             " +
    		" \"value\" : 0                           },            " +
    		"               \"duration\" : {                          " +
    		"    \"text\" : \"1 min\",                            " +
    		"  \"value\" : 0                           },           " +
    		"                \"end_location\" : {                    " +
    		"          \"lat\" : 47.681470,                        " +
    		"      \"lng\" : -122.327350                          " +
    		" },                           \"html_instructions\" : " +
    		"\"Walk \u003cb\u003enorth-west\u003c/b\u003e\",        " +
    		"                   \"polyline\" : {                   " +
    		"           \"points\" : \"ex_bH~`siV?A\"              " +
    		"             },                           \"start_location\"" +
    		" : {                              \"lat\" : 47.681470, " +
    		"                             \"lng\" : -122.327360  " +
    		"                         },                           " +
    		"\"travel_mode\" : \"WALKING\"                        " +
    		"}                     ],                    " +
    		" \"travel_mode\" : \"WALKING\"                  },    " +
    		"              {                     \"distance\" : {   " +
    		"                     \"text\" : \"2.0 mi\",             " +
    		"           \"value\" : 3142                     },       " +
    		"              \"duration\" : {                        \"text\"" +
    		" : \"11 mins\",                        \"value\" : 639  " +
    		"                   },                     \"end_location\" : " +
    		"{                        \"lat\" : 47.66135000000001,    " +
    		"                    \"lng\" : -122.312080                 " +
    		"    },                     \"html_instructions\" : \"Bus" +
    		" towards Mount Baker Transit Center, University District\"," +
    		"                     \"polyline\" : {                      " +
    		"  \"points\" : \"ex_bH|`siVIMjAwBZo@x@}@l@e@b@Uf@QXA\\CR?VJ^]" +
    		"fC}Br@m@zDiDXYtHaH@_B?y@?g@?K@Y?W@eA?o@?C?Q?y@@mC?_C@iC?_B?a@" +
    		"@sI@{@?{A?oB@yB@yB?uB?}A?Y~@?t@?tACJ?fA?hA?p@?nHCT?T?r@?D?b@?" +
    		"~@?v@?V?JA`G?tDAB?J?tKFX?xGBhA?~GDfGDpAA?P\"                  " +
    		"   },                     \"start_location\" : {              " +
    		"          \"lat\" : 47.681470,                        \"lng\"" +
    		" : -122.327350                     },                   " +
    		"  \"transit_details\" : {                       " +
    		" \"arrival_stop\" : {                           \"location\"" +
    		" : {                              \"lat\" : 47.66134640, " +
    		"                             \"lng\" : -122.312080        " +
    		"                   },                           \"name\" :" +
    		" \"15th Ave NE & NE 45th St\"                        },   " +
    		"                     \"arrival_time\" : {                  " +
    		"         \"text\" : \"13:58\",                         " +
    		"  \"time_zone\" : \"America/Los_Angeles\",                  " +
    		"         \"value\" : 1368997138                        },    " +
    		"                    \"departure_stop\" : {                  " +
    		"         \"location\" : {                             " +
    		" \"lat\" : 47.6814690,                             " +
    		" \"lng\" : -122.3273540                           },     " +
    		"                      \"name\" : \"East Green LK Dr N & " +
    		"Latona Ave NE\"                        },                  " +
    		"      \"departure_time\" : {                          " +
    		" \"text\" : \"13:48\",                           \"time_zone\"" +
    		" : \"America/Los_Angeles\",                         " +
    		"  \"value\" : 1368996499                        },      " +
    		"                  \"headsign\" : \"Mount Baker Transit Center," +
    		" University District\",                        \"line\" : {  " +
    		"                         \"agencies\" : [                     " +
    		"         {                                 \"name\" :" +
    		" \"Metro Transit\",                                " +
    		" \"phone\" : \"(206) 553-3000\",                    " +
    		"             \"url\" : \"http://metro.kingcounty.gov/\"  " +
    		"                            }                      " +
    		"    ],                           \"short_name\" : \"48\"," +
    		"                           \"url\" : " +
    		"\"http://metro.kingcounty.gov/tops/bus/schedules/s048_0_.html\"," +
    		"                           \"vehicle\" : {  " +
    		"                            \"icon\" : " +
    		"\"//maps.gstatic.com/mapfiles/transit/iw/6/bus.png\"," +
    		"                              \"name\" : \"Bus\",        " +
    		"                      \"type\" : \"BUS\"               " +
    		"            }                        },                  " +
    		"      \"num_stops\" : 10                     },         " +
    		"            \"travel_mode\" : \"TRANSIT\"                " +
    		"  }               ],               \"via_waypoint\" : [] " +
    		"           }         ],         \"overview_polyline\" : {" +
    		"            \"points\" : \"ex_bH~`siVIOfBgDx@}@l@e@jAg@v@E" +
    		"R?VJfD{CnFwEnI{H@yC@eBD}QFq]?sE?Y~@?jCCrA?`MCrB?zCAfMArWJ" +
    		"fPJpAA?P\"         },         \"warnings\" : [            " +
    		"\"Walking directions are in beta.    Use caution – This route " +
    		"may be missing sidewalks or pedestrian paths.\"         ],  " +
    		"       \"waypoint_order\" : []      }   ],   \"status\" : \"OK\"}";

}

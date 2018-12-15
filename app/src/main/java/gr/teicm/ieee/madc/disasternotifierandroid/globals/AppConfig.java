package gr.teicm.ieee.madc.disasternotifierandroid.globals;

public class AppConfig {
    // View Config
    public final static int SplashDuration = 3000;
    // NetworkService Config
    public final static String UserAgent = "Mozilla/5.0";
    public final static int ConnectTimeout = 5000;
    public final static int ReadTimeout = 10000;
    // Storage Files
    public final static String AuthFile = "auth.dat";
    // Default Preferences
    public final static String DefaultNearDistance = "5000";
    // Network URIs
    private final static String BackEndSys = "http" + "://";
    private final static String BackEndDomain = "snf-850534.vm.okeanos.grnet.gr:8080/";
    private final static String APIEndPoint = BackEndSys + BackEndDomain + "api/";
    // APIs
    public final static String APIRegister = APIEndPoint + "auth/register";
    public final static String APILogin = APIEndPoint + "auth/login";
    public final static String APIDisaster = APIEndPoint + "disasters/";
    public final static String APINear = APIEndPoint + "me/near/";
    public final static String APIMeLocation = APIEndPoint + "me/location";
    public final static String APIMeToken = APIEndPoint + "me/firebase";
    public static final String APIMeReports = APIEndPoint + "/me/reports";

}

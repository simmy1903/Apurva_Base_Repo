package api;

public class FrameworkContentTypeSwitcher implements ContentTypeSwitcher {

    public FrameworkContentTypeSwitcher () {

    }

    public String determineContentType(String url) {
        byte var3 = -1;
        switch (url.hashCode()) {
        case 0:
            if (url.equals("")) {
                var3 = 0;
            }
        default:
            switch (var3) {
            case 0:
                return null;
            default:
                return "application/json";

            }
        }
    }
}

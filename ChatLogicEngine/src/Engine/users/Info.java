package Engine.users;

import java.util.Map;

public class Info {

    private String active;
    private Map<String,String> users;

    public Info(String a, Map<String, String> _users)
    {
        active=a;
        users=_users;
    }

}

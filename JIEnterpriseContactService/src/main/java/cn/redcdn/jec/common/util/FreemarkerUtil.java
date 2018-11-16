package cn.redcdn.jec.common.util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

@SuppressWarnings("deprecation")
public class FreemarkerUtil {

    static Configuration cfg = new Configuration();

    static Template createOrgAccountSuccessTemplate = null;
    static Template resetOrgAccountSuccessTemplate = null;

    static {
        try {
            String parentPath = FreemarkerUtil.class.getClassLoader().getResource("").getPath();
            cfg.setDirectoryForTemplateLoading(new File(parentPath + File.separator + "template"));
            cfg.setDefaultEncoding("utf-8");

            createOrgAccountSuccessTemplate = cfg.getTemplate("createOrgAccountSuccess.ftl");
            resetOrgAccountSuccessTemplate = cfg.getTemplate("resetOrgAccountSuccess.ftl");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getCreateOrgAccountSuccessOutPut(String account, String password, int flag) {
        try {
            StringWriter sw = new StringWriter();
            Map<String, String> root = new HashMap<>();
            root.put("account", account);
            root.put("password", password);
            root.put("address", PropertiesUtil.getProperty("login.address"));
            if (flag == Constants.CREATE_ACCOUNT) {
                createOrgAccountSuccessTemplate.process(root, sw);
            } else if (flag == Constants.RESET_ACCOUNT) {
                resetOrgAccountSuccessTemplate.process(root, sw);
            }
            return sw.toString();
        } catch (Exception e) {
            return null;
        }
    }
}

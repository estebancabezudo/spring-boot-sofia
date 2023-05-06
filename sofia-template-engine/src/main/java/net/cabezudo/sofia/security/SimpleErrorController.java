package net.cabezudo.sofia.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SimpleErrorController implements ErrorController {
  private @Autowired ErrorAttributes errorAttributes;

  @RequestMapping("/error")
  public void error(WebRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<>();
    map.putAll(this.errorAttributes.getErrorAttributes(request, ErrorAttributeOptions.defaults()));

    System.out.println(map.get("path"));
    System.out.println(map.get("message"));
    System.out.println(map.get("timestamp").toString());
    System.out.println(map.get("trace"));

  }
}
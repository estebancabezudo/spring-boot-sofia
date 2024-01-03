package net.cabezudo.sofia;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.accounts.service.AccountManager;
import net.cabezudo.sofia.security.SofiaAuthorizedController;
import net.cabezudo.sofia.users.service.Group;
import net.cabezudo.sofia.users.service.SofiaUser;
import net.cabezudo.sofia.web.user.WebUserData;
import net.cabezudo.sofia.web.user.WebUserDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class ProtectedController extends SofiaAuthorizedController {
  private @Autowired WebUserDataManager webUserDataManager;
  private @Autowired AccountManager accountManager;

  @GetMapping("/protected")
  public ResponseEntity<?> protectedControllerExample(HttpServletRequest request, HttpServletResponse response) throws IOException {
    WebUserData webUserData = webUserDataManager.getFromSession(request);
    SofiaUser user = webUserData == null ? null : webUserData.getUser();

    Account account = accountManager.getAccount(request);
    ResponseEntity<?> result;

    /**
     * The condition validates that the account registered in the system has access to the
     * specified groups. If you only need one group, you can simply replace "ADMIN" with the
     * corresponding group. Group.ADMIN is a text string. You can substitute "Group.ADMIN" with
     * the text string containing the name of the group with permissions to access the
     * controller. If you want more groups, you can place them at the end of the method since
     * the argument is a list of parameters.
     */
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }
    return ResponseEntity.ok(user);
  }
}

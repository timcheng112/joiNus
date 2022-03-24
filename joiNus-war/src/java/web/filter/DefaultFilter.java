/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.filter;

import entity.AdminEntity;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author User
 */
@WebFilter(filterName = "DefaultFilter", urlPatterns = {"/*"})
public class DefaultFilter implements Filter {

    FilterConfig filterConfig;

    private static final String CONTEXT_ROOT = "/PointOfSaleSystemV54JsfAdvPf";

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession httpSession = httpServletRequest.getSession(true);
        String requestServletPath = httpServletRequest.getServletPath();

        if (httpSession.getAttribute("isLogin") == null) {
            httpSession.setAttribute("isLogin", false);
        }

        if (httpSession.getAttribute("isSuperAdmin") == null) {
            httpSession.setAttribute("isSuperAdmin", false);
        }

        Boolean isLogin = (Boolean) httpSession.getAttribute("isLogin");
        Boolean isSuperAdmin = (Boolean) httpSession.getAttribute("isSuperAdmin");

        if (!excludeLoginCheck(requestServletPath)) {
            if (isLogin == true) {
                AdminEntity currentAdminEntity = (AdminEntity) httpSession.getAttribute("currentAdminEntity");

                if (checkAccessRight(requestServletPath, currentAdminEntity.getIsSuperAdmin())) {
                    chain.doFilter(request, response);
                } else {
//                    httpServletResponse.sendRedirect(CONTEXT_ROOT + "/accessRightError.xhtml");
                }
            } else {
//                httpServletResponse.sendRedirect(CONTEXT_ROOT + "/accessRightError.xhtml");
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {

    }

    private Boolean checkAccessRight(String path, Boolean isSuperAdmin) {
        if (isSuperAdmin.equals(true)) { // allow for superadmin
            return true;
        }
//            if (path.equals("/cashierOperation/checkout.xhtml")
//                    || path.equals("/cashierOperation/voidRefund.xhtml")
//                    || path.equals("/cashierOperation/viewMySaleTransactions.xhtml")) {
//                return true;
//            } else {
//                return false;
//            }
//        } else if (accessRight.equals(AccessRightEnum.MANAGER)) {
//            if (path.equals("/cashierOperation/checkout.xhtml")
//                    || path.equals("/cashierOperation/voidRefund.xhtml")
//                    || path.equals("/cashierOperation/viewMySaleTransactions.xhtml")
//                    || path.equals("/systemAdministration/staffManagement.xhtml")
//                    || path.equals("/systemAdministration/productManagement.xhtml")
//                    || path.equals("/systemAdministration/searchProductsByName.xhtml")
//                    || path.equals("/systemAdministration/filterProductsByCategory.xhtml")
//                    || path.equals("/systemAdministration/filterProductsByTags.xhtml")) {
//                return true;
//            } else {
//                return false;
//            }
//        }

        return false;
    }

    private Boolean excludeLoginCheck(String path) {
        if (path.equals("/index.xhtml")
//                || path.equals("/accessRightError.xhtml")
//                || path.startsWith("/javax.faces.resource")
//                || path.equals("/customerIndex.xhtml")
//                || path.startsWith("/customerOperation")
                ) {
            return true;
        } else {
            return false;
        }
    }

}

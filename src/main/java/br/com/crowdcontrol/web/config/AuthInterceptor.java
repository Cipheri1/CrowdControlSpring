package br.com.crowdcontrol.web.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/** Barra qualquer chamada à API de quem não fez login (exceto o próprio login). */
public class AuthInterceptor implements HandlerInterceptor {

    public static final String SESSAO_USUARIO = "usuarioLogado";

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        HttpSession sessao = req.getSession(false);
        if (sessao != null && sessao.getAttribute(SESSAO_USUARIO) != null) {
            return true;
        }
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write("{\"erro\":\"Não autenticado\"}");
        return false;
    }
}

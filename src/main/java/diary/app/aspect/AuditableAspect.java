package diary.app.aspect;

import diary.app.config.StaticConfig;
import diary.app.dao.AuditDao;
import diary.app.dto.AuditItem;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import static diary.app.servlet.GetParams.LOGIN_PARAM;

@Aspect
public class AuditableAspect {
    @Pointcut("within(@diary.app.annotations.Auditable *) && execution(* * (..))")
    public void annotatedByAuditable() {}

    @Around("annotatedByAuditable()")
    public Object audit(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Signature signature = proceedingJoinPoint.getSignature();
        Object firstArg = proceedingJoinPoint.getArgs()[0];
        Object res = proceedingJoinPoint.proceed();
        AuditDao auditDao = StaticConfig.SERVICES_FACTORY.getDaoFactory().getAuditDao();
        auditDao.addAuditItem(new AuditItem(resolveUser(firstArg), signature.toString(), ""));
        return res;
    }

    public String resolveUser(Object reqArg) {
        try {
            if (reqArg instanceof HttpServletRequest) {
                HttpServletRequest req = (HttpServletRequest) reqArg;
                return req.getParameter(LOGIN_PARAM);
            }
        } catch (Exception e) {
            //do nothing as we failed to get param for audit
        }
        return "unknown";
    }
}

package annotations;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class LogginAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogginAspect.class);

    /**
     * <p>Metodo: Definicion del PointCut controller.</p>
     * <p>Descripcion: Este metodo es la definicion del pointcut para identificar todos los metodos anotados con @InfoLogger.</p>
     */
    @Pointcut("@annotation(co.com.microservice.application.anotaciones.InfoLogger)")
    public void infoLoggerPointcut() {}

    /**
     * <p>Metodo: Definicion del PointCut controller.</p>
     * <p>Descripcion: Este metodo es la definicion del pointcut para identificar todas las clases anotadas con @Controller.</p>
     */
    //@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    //public void controllerPointcut() {}

    /**
     * <p>Metodo: Definicion del PointCut requestMapping.</p>
     * <p>Descripcion: Este metodo es la definicion del pointcut para identificar todas las clases anotadas con @RequestMapping.</p>
     */
    //@Pointcut("within(@org.springframework.web.bind.annotation.RequestMapping *)")
    //public void requestMappingPointcut() {}

    /**
     * <p>Metodo: Definicion del PointCut.</p>
     * <p>Descripcion: Este metodo es la definicion del pointcut para la ejecución de todos lo metodos DAOImpl y que comienzan por get.</p>
     */
    //@Pointcut("execution(public * co.com.microservice.application.controller.*.obtener*())")
    //public void operacionesGetPointcut(){};

    /**
     * <p>Metodo: Antes de la Operacion.</p>
     * <p>Descripcion: Este metodo se ejecuta antes de cada metodo.</p>
     * @param jp de tipo JoinPoint con la informacion de Objetos de entrada y salida del metodo
     */
    //@Before("infoLoggerPointcut() && controllerPointcut() && requestMappingPointcut() && operacionesGetPointcut()")
    @Before("infoLoggerPointcut()")
    public void beforeOper(JoinPoint jp){
        // Impresion en el log
        try{
            Object result = jp.getArgs();
            ObjectMapper objectMapper = new ObjectMapper();
            logger.debug("*============================ BEFORE > > > {}", objectMapper.writeValueAsString(result));
        }catch (JsonProcessingException jpe){
            logger.error("JsonGenerationException > > > {}", jpe.toString());
        }catch (Throwable e){
            logger.error("*============================ Throwable > > > {}", e.toString());
        }
    }

    /**
     * <p>Metodo: Despues de la Operacion.</p>
     * <p>Descripcion: Este metodo se ejecuta antes de cada metodo.</p>
     * @param jp de tipo JoinPoint con la informacion de Objetos de entrada y salida del metodo
     */
    //@After("infoLoggerPointcut() && controllerPointcut() && requestMappingPointcut() && operacionesGetPointcut()")
    @After("infoLoggerPointcut()")
    public void afterOper(JoinPoint jp){
        // Impresion en el log
        try{
            Object result = jp.getArgs();
            ObjectMapper objectMapper = new ObjectMapper();
            logger.debug("*============================ AFTER > > > {}", objectMapper.writeValueAsString(result));
        }catch (JsonProcessingException jpe){
            logger.error("*JsonGenerationException > > > {}", jpe.toString());
        }catch (Throwable e){
            logger.error("*============================ Throwable > > > {}", e.toString());
        }

    }

    /**
     * <p>Metodo: Control de Excepciones.</p>
     * <p>Descripcion: Este metodo se ejecuta cuando se lanza una excepcion.</p>
     * @param e de tipo Exception
     */
    //@AfterThrowing(pointcut="infoLoggerPointcut() && controllerPointcut() && requestMappingPointcut() && operacionesGetPointcut()", throwing="error")
    @AfterThrowing(pointcut="infoLoggerPointcut()", throwing="error")
    public void controlException(JoinPoint joinPoint, Throwable error){
        // Impresion en el log
        logger.debug("*============================ SE PRODUJO UNA EXCEPCION > > > {} CAUSA {}", joinPoint.getSignature().getName(), error);
    }

    /**
     * <p>Metodo: Impresion de Parametros de entrada.</p>
     * <p>Descripcion: Metodo que muestra los valores de los parametros de entrada de cada procedimiento.</p>
     * @param pjp del tipo ProceedingJoinPoint
     * @return the object
     * @throws Throwable the throwable
     */
    //@Around("infoLoggerPointcut() && controllerPointcut() && requestMappingPointcut() && operacionesGetPointcut()")
    @Around("infoLoggerPointcut()")
    public Object logMethod(ProceedingJoinPoint pjp) throws Throwable{
        // Definicion del objeto a retornar
        Object retval =  null;
        try{
            // Definicion del String que imprimira los valores de los parametros
            StringBuffer stb = new StringBuffer();
            // Construccion del String
            stb.append("============================ INICIANDO METODO > > > " + pjp.getTarget().getClass().getName());
            stb.append("(");
            // Obtenemos los parametros de entrada de cada procedimiento
            Object[] arg = pjp.getArgs();

            // Recorremos los parametros para extraer sus valores
            for (int i = 0; i < arg.length; i++){
                stb.append(arg[i]).append(",");
            }

            // Validamos la longitud de los parametros
            if (arg.length > 0) {
                stb.deleteCharAt(stb.length() - 1);
            }
            // Cerramos el String para la impresion de los parametros
            stb.append(")");
            // Asignamos los objetos para retornar
            retval = pjp.proceed();
            // Imprimimos en el Log
            logger.debug("*============================ AROUND > > > {}",stb.toString());
        }catch(Throwable tr){
            logger.error("============================ ERROR > > > {}", tr);
            throw tr;
        }
        // Retornamos el valor
        return retval;
    }

    /**
     * <p>Metodo: Impresion en el log despues de retornar los valores.</p>
     * <p>Descripcion: Metodo que imprime en el log despues de retornar los valores.</p>
     * @param jp del tipo JoinPoint
     * @param result del tipo Object
     * @throws JsonProcessingException
     */
    //@AfterReturning(pointcut = "infoLoggerPointcut() && controllerPointcut() && requestMappingPointcut() && operacionesGetPointcut()", returning="result")
    @AfterReturning(pointcut = "infoLoggerPointcut()", returning="result")
    public void logAfterReturning(JoinPoint jp, Object result){
        // Impresion en el log
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            logger.debug("*============================ DESPUES DEL RETURNING > > > {}", objectMapper.writeValueAsString(result));
        }catch (JsonProcessingException jpe){
            logger.error("JsonGenerationException > > > {}", jpe.toString());
        }

    }

}
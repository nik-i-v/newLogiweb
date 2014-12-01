package ru.tsystems.javaschool.logiweb.lw.ui.validators;

        import javax.faces.application.FacesMessage;
        import javax.faces.component.UIComponent;
        import javax.faces.context.FacesContext;
        import javax.faces.validator.FacesValidator;
        import javax.faces.validator.Validator;
        import javax.faces.validator.ValidatorException;
        import java.util.regex.Matcher;
        import java.util.regex.Pattern;
/**
 * This class checks is a licese matches to the correct format.
 *
 * @author Irina Nikulina
 */
@FacesValidator("licenseValidator")
public class LicenseValidator implements Validator {

    private static final String NAME_PATTERN = "^\\d{11}$";

    private Pattern pattern;
    private Matcher matcher;

    /**
     * Constructs a new object of the LicenseValidator and sets a pattern to enter correct data.
     */
    public LicenseValidator(){
        pattern = Pattern.compile(NAME_PATTERN);
    }

    /**
     * Validates an entered information.
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException
     */
    @Override
    public void validate(FacesContext context, UIComponent component,
                         Object value) throws ValidatorException {

        matcher = pattern.matcher(value.toString());
        if(!matcher.matches()){

            FacesMessage msg =
                    new FacesMessage(" License length should be 11");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);

        }

    }
}


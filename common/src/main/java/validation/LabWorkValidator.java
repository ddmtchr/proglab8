package validation;

import stored.LabWork;
import utility.LabWorkStatic;

public class LabWorkValidator {
    public static boolean isValid(LabWork lw) {
        return IdValidator.isValid(lw.getId()) && NameValidator.isValid(lw.getName())
                && lw.getCoordinates() != null && lw.getCoordinates().getY() != null
                && XValidator.isValid(lw.getCoordinates().getX())
                && YValidator.isValid(lw.getCoordinates().getY()) && lw.getCreationDate() != null
                && MinPointValidator.isValid(lw.getMinimalPoint())
                && AveragePointValidator.isValid(lw.getAveragePoint()) && lw.getDifficulty() != null;
    }

    public static boolean isValid(LabWorkStatic lws) {
        return NameValidator.isValid(lws.getName())
                && lws.getCoordinates() != null && lws.getCoordinates().getY() != null
                && XValidator.isValid(lws.getCoordinates().getX())
                && YValidator.isValid(lws.getCoordinates().getY())
                && MinPointValidator.isValid(lws.getMinimalPoint())
                && AveragePointValidator.isValid(lws.getAveragePoint()) && lws.getDifficulty() != null;
    }
}

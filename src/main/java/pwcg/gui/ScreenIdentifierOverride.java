package pwcg.gui;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScreenIdentifierOverride
{
    private ScreenIdentifier screenIdentifier;
    private String configuredImageName;

    public ScreenIdentifierOverride(ScreenIdentifier screenIdentifier)
    {
        this.screenIdentifier = screenIdentifier;
        this.configuredImageName = "";
    }
}

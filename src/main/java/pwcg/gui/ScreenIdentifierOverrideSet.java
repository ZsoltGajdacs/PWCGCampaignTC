package pwcg.gui;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScreenIdentifierOverrideSet
{
    private Map<ScreenIdentifier, String> configuredOverrides = new HashMap<>();
}

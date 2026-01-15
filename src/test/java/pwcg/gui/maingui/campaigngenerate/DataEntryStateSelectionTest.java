package pwcg.gui.maingui.campaigngenerate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Assertions;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignData;
import pwcg.campaign.CampaignMode;
import pwcg.core.exception.PWCGException;
import pwcg.gui.maingui.campaigngenerate.CampaignGeneratorState.CampaignGeneratorWorkflow;
import pwcg.gui.maingui.campaigngenerate.NewCrewMemberState.CrewMemberGeneratorWorkflow;

@ExtendWith(MockitoExtension.class)
public class DataEntryStateSelectionTest
{
    @Test
    public void campaignGeneratorStepSelectionTest() throws PWCGException
    {
        CampaignGeneratorDO generatorDO = new CampaignGeneratorDO();
        generatorDO.setCampaignMode(CampaignMode.CAMPAIGN_MODE_SINGLE);

        CampaignGeneratorState state = new CampaignGeneratorState(generatorDO);
        state.buildStateStack();
        state.setCurrentStep(CampaignGeneratorWorkflow.CHOOSE_ROLE);

        Assertions.assertEquals(CampaignGeneratorWorkflow.CHOOSE_ROLE, state.getCurrentStep());
        Assertions.assertTrue(state.getStateStack().contains(CampaignGeneratorWorkflow.CHOOSE_ROLE));
    }

    @Test
    public void crewMemberGeneratorStepSelectionTest() throws PWCGException
    {
        Campaign campaign = Mockito.mock(Campaign.class);
        CampaignData campaignData = new CampaignData();
        campaignData.setCampaignMode(CampaignMode.CAMPAIGN_MODE_SINGLE);
        Mockito.when(campaign.getCampaignData()).thenReturn(campaignData);

        NewCrewMemberGeneratorDO generatorDO = new NewCrewMemberGeneratorDO();
        NewCrewMemberState state = new NewCrewMemberState(campaign, generatorDO);
        state.buildStateStack();
        state.setCurrentStep(CrewMemberGeneratorWorkflow.CHOOSE_RANK);

        Assertions.assertEquals(CrewMemberGeneratorWorkflow.CHOOSE_RANK, state.getCurrentStep());
        Assertions.assertTrue(state.getStateStack().contains(CrewMemberGeneratorWorkflow.CHOOSE_RANK));
    }
}

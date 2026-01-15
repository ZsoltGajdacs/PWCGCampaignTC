package pwcg.aar.ui.display.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pwcg.aar.ui.events.model.TransferEvent;

@Getter
@Setter
public class TransferPanelData
{
    private TransferEvent playerTransferEvent = null;
    private List<TransferEvent> transfers = new ArrayList<>();

    public void merge(TransferPanelData transferPanelData)
    {
        if (transferPanelData.getPlayerTransferEvent() != null)
        {
            playerTransferEvent = transferPanelData.getPlayerTransferEvent();
        }
        transfers.addAll(transferPanelData.getTransfers());
    }
}

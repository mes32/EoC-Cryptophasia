/*
    CryptophasiaTransmission.java

    
 */

package cryptophasia.networking.transmission;

import cryptophasia.exception.*;

public interface CryptophasiaTransmission {

    public String encode();
    public String replyWithAccept(boolean accept);
    public boolean isAccepted(String reply);
}
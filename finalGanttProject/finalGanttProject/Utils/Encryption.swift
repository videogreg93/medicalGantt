//
//  Encryption.swift
//  finalGanttProject
//
//  Created by Gregory Fournier on 2018-05-22.
//  Copyright © 2018 Gregory Fournier. All rights reserved.
//

import Foundation
import SwiftyRSA

class Encryption {
    public static func decrypt(_ data: String) throws -> String {
        let privateKey = try PrivateKey(pemNamed: "private_key" )
        let encrypted = try EncryptedMessage(base64Encoded: data)
        let clear = try encrypted.decrypted(with: privateKey, padding: .PKCS1)
        
        // Then you can use:
        //let data = clear.data
        //let base64String = clear.base64String
        do {
            let string = try clear.string(encoding: .utf8)
            print("Decoded = " + string);
            return string;
        } catch {
            // Error handling
        }
        return "";
    }
}

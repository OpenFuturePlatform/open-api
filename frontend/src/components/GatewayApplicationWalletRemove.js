import {EntityRemove} from "./EntityRemove";
import {t} from "../utils/messageTexts";
import {Divider} from "semantic-ui-react";
import React from "react";

export const GatewayApplicationWalletRemove = ({ onSubmit }) => (
    <EntityRemove onSubmit={onSubmit} header="Remove Wallet address">
        <div>
            {t('sure to remove address')}
            <Divider />
            <span>{t('it may take a while')}</span>
        </div>
    </EntityRemove>
);
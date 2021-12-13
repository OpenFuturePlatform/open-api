import {EntityRemove} from "./EntityRemove";
import {t} from "../utils/messageTexts";
import React from "react";

export const GatewayApplicationRemove = ({ onSubmit }) => (
    <EntityRemove onSubmit={onSubmit} header="Delete Application">
        <div>
            {t('sure to delete Application')}
        </div>
    </EntityRemove>
);

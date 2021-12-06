import {EntityRemove} from "./EntityRemove";
import {t} from "../utils/messageTexts";
import React from "react";

export const GatewayApplicationRemove = ({ onSubmit }) => (
    <EntityRemove onSubmit={onSubmit} header="Delete Gateway">
        <div>
            {t('sure to delete Gateway')}
        </div>
    </EntityRemove>
);
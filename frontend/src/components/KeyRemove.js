import React from 'react';
import { EntityRemove } from './EntityRemove';
import { t } from '../utils/messageTexts';

export const KeyRemove = ({ onSubmit }) => (
  <EntityRemove onSubmit={onSubmit} header="Key Deactivation">
    <div>{t('sure to deactivate key')}</div>
  </EntityRemove>
);

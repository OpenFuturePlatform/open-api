import React from 'react';
import { EntityRemove } from './EntityRemove';
import { Divider } from 'semantic-ui-react';
import { t } from '../utils/messageTexts';

export const ShareHolderRemove = ({ onSubmit }) => (
  <EntityRemove onSubmit={onSubmit} header="Remove Share Holder">
    <div>
      {t('sure to remove shareholder')}
      <Divider />
      <span>{t('it may take a while')}</span>
    </div>
  </EntityRemove>
);

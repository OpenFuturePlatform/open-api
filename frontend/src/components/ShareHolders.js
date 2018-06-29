import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import 'react-table/react-table.css';
import '../css/table.css';
import { EtherscanLink } from '../components-ui/EtherscanLink';
import { Segment } from 'semantic-ui-react';
import {
  addShareHolder,
  editShareHolder,
  fetchShareHolders,
  removeShareHolder
} from '../actions/shareHolders';
import { ShareHolderSave } from './ShareHolderSave';
import { Table } from '../components-ui/Table';
import { ShareHolderRemove } from './ShareHolderRemove';

const getColumns = (onEdit, onRemove) => [
  {
    Header: 'Share Holder Address',
    accessor: 'address',
    Cell: ({ value }) => <EtherscanLink>{value}</EtherscanLink>,
    sortable: false
  },
  {
    Header: 'Holder Share',
    accessor: 'share',
    width: 200,
    Cell: ({ value }) => <span>{value}%</span>,
    sortable: false
  },
  {
    Header: '',
    accessor: 'address',
    width: 150,
    Cell: ({ value, original }) => (
      <span>
        <ShareHolderSave
          editType
          shareHolder={original}
          onSubmit={devShare => onEdit(devShare)}
        />{' '}
        <ShareHolderRemove onSubmit={() => onRemove(value)} />
      </span>
    ),
    sortable: false
  }
];

export class ShareHoldersComponent extends Component {
  componentDidMount() {
    const { scaffold } = this.props;
    this.props.actions.fetchShareHolders(scaffold);
  }

  onAddShareHolder = shareHolder => {
    const { scaffold } = this.props;
    return this.props.actions.addShareHolder(scaffold, shareHolder);
  };

  onEditShareHolder = shareHolder => {
    const { scaffold } = this.props;
    return this.props.actions.editShareHolder(scaffold, shareHolder);
  };

  onRemoveShareHolder = holderAddress => {
    const { scaffold } = this.props;
    return this.props.actions.removeShareHolder(scaffold, holderAddress);
  };

  render() {
    const { shareHolders } = this.props;
    const columns = getColumns(
      this.onEditShareHolder,
      this.onRemoveShareHolder
    );

    return (
      <div className="table-with-add">
        <ShareHolderSave onSubmit={this.onAddShareHolder} />
        <Segment attached styles={{ padding: 0 }}>
          <Table data={shareHolders} columns={columns} />
        </Segment>
      </div>
    );
  }
}

const mapStateToProps = ({ scaffoldsById, byApiMethod }, { scaffold }) => {
  const scaffoldSet = scaffoldsById[scaffold.address] || {};
  const shareHolders = scaffoldSet.shareHolders || [];
  return { scaffold, shareHolders, byApiMethod };
};

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators(
    {
      fetchShareHolders,
      addShareHolder,
      editShareHolder,
      removeShareHolder
    },
    dispatch
  )
});

export const ShareHolders = connect(
  mapStateToProps,
  mapDispatchToProps
)(ShareHoldersComponent);

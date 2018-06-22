import React, {Component} from 'react';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import ReactTable from 'react-table';
import 'react-table/react-table.css';
import '../css/table.css';
import {EtherscanLink} from '../components-ui/EtherscanLink';
import {Segment} from 'semantic-ui-react';
import {addShareHolder, editShareHolder, fetchShareHolders, removeShareHolder} from '../actions/shareHolders';
import {ShareHolderSave} from './ShareHolderSave';
import {RemoveDevShare} from './RemoveDevShare';

const getColumns = (scaffold, onEdit, onRemove) => [
  {
    Header: 'Share Holder Address',
    accessor: 'address',
    Cell: ({value}) => <EtherscanLink>{value}</EtherscanLink>,
    sortable: false
  },
  {
    Header: 'Holder Share',
    accessor: 'share',
    width: 200,
    Cell: ({value}) => <span>{value}%</span>,
    sortable: false
  },
  {
    Header: '',
    accessor: 'address',
    width: 150,
    Cell: ({value, original}) => (
      <span>
        <ShareHolderSave editType shareHolder={original} onSubmit={(devShare) => onEdit(devShare)} />{' '}
        <RemoveDevShare onSubmit={() => onRemove(value)} />
      </span>
    ),
    sortable: false,
  }
];

export class ShareHoldersComponent extends Component {

  componentDidMount() {
    const {scaffold} = this.props;
    this.props.actions.fetchShareHolders(scaffold);
  }

  onAddShareHolder = (shareHolder) => {
    const {scaffold} = this.props;
    return this.props.actions.addShareHolder(scaffold, shareHolder);
  };

  onEditShareHolder = (shareHolder) => {
    const {scaffold} = this.props;
    return this.props.actions.editShareHolder(scaffold, shareHolder);
  };

  onRemoveShareHolder = (holderAddress) => {
    const {scaffold} = this.props;
    return this.props.actions.removeShareHolder(scaffold, holderAddress);
  };

  render() {
    const {shareHolders, scaffold} = this.props;

    return (
      <div className="table-with-add">
        <ShareHolderSave onSubmit={this.onAddShareHolder}/>
        <Segment attached styles={{padding: 0}}>
          <ReactTable data={shareHolders} columns={getColumns(scaffold, this.onEditShareHolder, this.onRemoveShareHolder)}
                      className="-striped" showPagination={false} resizable={false} minRows={1}
                      pageSize={shareHolders.length}
          />
        </Segment>
      </div>
    );
  }
}

const mapStateToProps = ({ScaffoldsById, byApiMethod}, {scaffold}) => {
  const scaffoldSet = ScaffoldsById[scaffold.address] || {};
  const shareHolders = scaffoldSet.shareHolders || [];
  return ({scaffold, shareHolders, byApiMethod});
};

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators({
    fetchShareHolders,
    addShareHolder,
    editShareHolder,
    removeShareHolder
  }, dispatch)
});

export const ShareHolders = connect(mapStateToProps, mapDispatchToProps)(ShareHoldersComponent);

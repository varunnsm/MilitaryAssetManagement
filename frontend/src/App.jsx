import React, { useEffect, useState } from 'react'
import api from './api'
import { LayoutDashboard, ShoppingCart, ArrowLeftRight, UserRound, PackageX, ShieldCheck, LogOut, Menu, X, RefreshCw } from 'lucide-react'

const today = new Date().toISOString().slice(0, 10)
const monthStart = new Date(new Date().getFullYear(), new Date().getMonth(), 1).toISOString().slice(0, 10)

function Login({ onLogin }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    async function submit(e) {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            const r = await api.post('/auth/login', { username, password });
            localStorage.setItem('token', r.data.token);
            localStorage.setItem('user', JSON.stringify(r.data));
            onLogin(r.data);
        } catch (e) {
            setError(e.response?.data?.message || 'Login failed');
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="login-shell">
            <div className="login-card">
                <div className="brand-mark">M</div>

                <h1>Military Asset Management</h1>

                <p className="muted">
                    Secure logistics & accountability platform
                </p>

                <form onSubmit={submit} autoComplete="off">
                    <label>Username</label>

                    <input
                        type="text"
                        name="login-user"
                        autoComplete="off"
                        value={username}
                        onChange={e => setUsername(e.target.value)}
                    />

                    <label>Password</label>

                    <input
                        type="password"
                        name="login-pass"
                        autoComplete="new-password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                    />

                    {error && <div className="error">{error}</div>}

                    <button
                        className="primary full"
                        disabled={loading}
                    >
                        {loading ? 'Signing in...' : 'Sign in'}
                    </button>
                </form>
            </div>
        </div>
    );
}

function App() {
    const [user, setUser] = useState(
        () => JSON.parse(localStorage.getItem('user') || 'null')
    );

    const [page, setPage] = useState(
        () => localStorage.getItem('currentPage') || 'dashboard'
    );

    const [mobileOpen, setMobileOpen] = useState(false);

    function handleLogin(userData) {
        setUser(userData);
        setPage('dashboard');
        localStorage.setItem('currentPage', 'dashboard');
    }

    function logout() {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        localStorage.removeItem('currentPage');

        setUser(null);
        setPage('dashboard');
        setMobileOpen(false);
    }

    if (!user) {
        return <Login onLogin={handleLogin} />;
    }

    const role = user.role;

    const items = [
        ['dashboard', 'Dashboard', LayoutDashboard, true],
        ['purchases', 'Purchases', ShoppingCart, true],
        ['transfers', 'Transfers', ArrowLeftRight, true],
        ['assignments', 'Assignments', UserRound,
            role === 'ADMIN' || role === 'BASE_COMMANDER'],
        ['expenditures', 'Expenditures', PackageX,
            role === 'ADMIN' || role === 'BASE_COMMANDER'],
        ['audit', 'Audit Logs', ShieldCheck,
            role === 'ADMIN']
    ];

    const allowedPages = items
        .filter(x => x[3])
        .map(x => x[0]);

    const currentPage = allowedPages.includes(page)
        ? page
        : 'dashboard';

    return (
        <div className="app-shell">

            <aside className={'sidebar ' + (mobileOpen ? 'open' : '')}>

                <div className="sidebar-top">
                    <div className="brand">
                        <span>M</span>
                        <div>
                            <b>MAMS</b>
                            <small>Asset Management</small>
                        </div>
                    </div>

                    <button
                        className="icon-btn mobile-only"
                        onClick={() => setMobileOpen(false)}
                    >
                        <X />
                    </button>
                </div>

                <nav>
                    {items
                        .filter(x => x[3])
                        .map(([id, label, Icon]) => (
                            <button
                                type="button"
                                key={id}
                                className={
                                    currentPage === id
                                        ? 'nav active'
                                        : 'nav'
                                }
                                onClick={() => {
                                    setPage(id);
                                    localStorage.setItem(
                                        'currentPage',
                                        id
                                    );
                                    setMobileOpen(false);
                                }}
                            >
                                <Icon size={18} />
                                {label}
                            </button>
                        ))}
                </nav>

                <div className="sidebar-user">
                    <div className="avatar">
                        {user.fullName?.[0] || 'U'}
                    </div>

                    <div>
                        <b>{user.fullName}</b>
                        <small>
                            {user.role.replaceAll('_', ' ')}
                        </small>
                    </div>
                </div>

            </aside>

            {mobileOpen && (
                <div
                    className="overlay"
                    onClick={() => setMobileOpen(false)}
                />
            )}

            <main className="main">

                <header>

                    <button
                        className="icon-btn mobile-menu"
                        onClick={() => setMobileOpen(true)}
                    >
                        <Menu />
                    </button>

                    <div>
                        <h2>
                            {items.find(x => x[0] === currentPage)?.[1]
                                || 'Dashboard'}
                        </h2>

                        <p>
                            {user.baseName
                                ? `${user.baseName} • `
                                : ''}
                            {user.role.replaceAll('_', ' ')}
                        </p>
                    </div>

                    <button
                        className="logout"
                        onClick={logout}
                    >
                        <LogOut size={16} />
                        Logout
                    </button>

                </header>

                <div className="content">

                    {currentPage === 'dashboard' && (
                        <Dashboard user={user} />
                    )}

                    {currentPage === 'purchases' && (
                        <Purchases user={user} />
                    )}

                    {currentPage === 'transfers' && (
                        <Transfers user={user} />
                    )}

                    {currentPage === 'assignments' && (
                        <Assignments user={user} />
                    )}

                    {currentPage === 'expenditures' && (
                        <Expenditures user={user} />
                    )}

                    {currentPage === 'audit' && (
                        <Audit />
                    )}

                </div>

            </main>

        </div>
    );
}

function Filters({ bases, types, filters, setFilters, admin = true, onApply }) {
    return <div className="filters"><div><label>From</label><input type="date" value={filters.from} onChange={e => setFilters({ ...filters, from: e.target.value })} /></div><div><label>To</label><input type="date" value={filters.to} onChange={e => setFilters({ ...filters, to: e.target.value })} /></div>{admin && <div><label>Base</label><select value={filters.baseId} onChange={e => setFilters({ ...filters, baseId: e.target.value })}><option value="">All bases</option>{bases.map(b => <option key={b.id} value={b.id}>{b.name}</option>)}</select></div>}<div><label>Equipment</label><select value={filters.equipmentTypeId} onChange={e => setFilters({ ...filters, equipmentTypeId: e.target.value })}><option value="">All equipment</option>{types.map(t => <option key={t.id} value={t.id}>{t.name}</option>)}</select></div><button className="secondary" onClick={onApply}><RefreshCw size={15} /> Apply</button></div>
}

function Dashboard({ user }) {
    const [bases, setBases] = useState([]);
    const [types, setTypes] = useState([]);
    const [filters, setFilters] = useState({
        from: monthStart,
        to: today,
        baseId: '',
        equipmentTypeId: ''
    });
    const [data, setData] = useState(null);
    const [showNet, setShowNet] = useState(false);

    const load = async () => {
        const p = {
            from: filters.from,
            to: filters.to
        };

        if (filters.baseId) {
            p.baseId = filters.baseId;
        }

        if (filters.equipmentTypeId) {
            p.equipmentTypeId = filters.equipmentTypeId;
        }

        const r = await api.get('/dashboard', {
            params: p
        });

        setData(r.data);
    };

    useEffect(() => {
        Promise.all([
            api.get('/meta/bases'),
            api.get('/meta/equipment-types')
        ]).then(([b, t]) => {
            setBases(b.data);
            setTypes(t.data);
        });

        load();
    }, []);

    useEffect(() => {
        if (user.role !== 'ADMIN') {
            setFilters(f => ({
                ...f,
                baseId: String(user.baseId || '')
            }));
        }
    }, []);

    return (
        <>
            <Filters
                bases={bases}
                types={types}
                filters={filters}
                setFilters={setFilters}
                admin={user.role === 'ADMIN'}
                onApply={load}
            />

            {data && (
                <>
                    <div className="metrics">
                        <Metric
                            title="Opening Balance"
                            value={data.openingBalance}
                        />

                        <Metric
                            title="Purchases"
                            value={data.purchases}
                        />

                        <Metric
                            title="Transfer In"
                            value={data.transferIn}
                        />

                        <Metric
                            title="Transfer Out"
                            value={data.transferOut}
                        />

                        <Metric
                            title="Net Movement"
                            value={data.netMovement}
                            clickable
                            onClick={() => setShowNet(true)}
                        />

                        <Metric
                            title="Assigned"
                            value={data.assigned}
                        />

                        <Metric
                            title="Expended"
                            value={data.expended}
                        />

                        <Metric
                            title="Closing Balance"
                            value={data.closingBalance}
                            highlight
                        />
                    </div>

                    <div className="card">
                        <div className="card-head">
                            <div>
                                <h3>Inventory summary</h3>
                                <p>
                                    Opening + Net Movement − Expended = Closing
                                </p>
                            </div>
                        </div>

                        <div className="formula">
                            <span>{data.openingBalance}</span>
                            <b>+</b>
                            <span>{data.netMovement}</span>
                            <b>−</b>
                            <span>{data.expended}</span>
                            <b>=</b>
                            <strong>{data.closingBalance}</strong>
                        </div>
                    </div>
                </>
            )}

            {showNet && (
                <Modal
                    title="Net Movement details"
                    onClose={() => setShowNet(false)}
                >
                    <div className="detail-row">
                        <span>Purchases</span>
                        <b>+{data.purchases}</b>
                    </div>

                    <div className="detail-row">
                        <span>Transfer In</span>
                        <b>+{data.transferIn}</b>
                    </div>

                    <div className="detail-row">
                        <span>Transfer Out</span>
                        <b>−{data.transferOut}</b>
                    </div>

                    <div className="detail-total">
                        <span>Net Movement</span>
                        <b>{data.netMovement}</b>
                    </div>
                </Modal>
            )}
        </>
    );
}

function Metric({ title, value, clickable, onClick, highlight }) { return <div className={'metric ' + (clickable ? 'clickable' : '') + (highlight ? ' highlight' : '')} onClick={onClick}><span>{title}</span><strong>{value?.toLocaleString?.() ?? value}</strong>{clickable && <small>Click for details</small>}</div> }
function Modal({ title, onClose, children }) { return <div className="modal-backdrop" onClick={onClose}><div className="modal" onClick={e => e.stopPropagation()}><div className="modal-head"><h3>{title}</h3><button className="icon-btn" onClick={onClose}><X /></button></div>{children}</div></div> }

function useMeta() { const [bases, setBases] = useState([]), [types, setTypes] = useState([]); useEffect(() => { Promise.all([api.get('/meta/bases'), api.get('/meta/equipment-types')]).then(([b, t]) => { setBases(b.data); setTypes(t.data) }) }, []); return { bases, types } }

function Table({ headers, rows }) { return <div className="table-wrap"><table><thead><tr>{headers.map(h => <th key={h}>{h}</th>)}</tr></thead><tbody>{rows.length ? rows.map((r, i) => <tr key={i}>{r.map((c, j) => <td key={j}>{c}</td>)}</tr>) : <tr><td colSpan={headers.length} className="empty">No records found</td></tr>}</tbody></table></div> }

function PageShell({ title, description, children }) { return <div><div className="page-intro"><div><h3>{title}</h3><p>{description}</p></div></div>{children}</div> }

function Purchases({ user }) {
    const { bases, types } = useMeta();

    const [rows, setRows] = useState([]);
    const [open, setOpen] = useState(false);

    const [form, setForm] = useState({
        baseId: user.role === 'ADMIN' ? '' : String(user.baseId || ''),
        equipmentTypeId: '',
        quantity: 1,
        purchaseDate: today,
        referenceNumber: ''
    });

    const load = async () => {
        const r = await api.get('/purchases');
        setRows(r.data);
    };

    useEffect(() => {
        load();
    }, []);

    async function save(e) {
        e.preventDefault();

        try {
            await api.post('/purchases', {
                ...form,
                baseId: Number(form.baseId),
                equipmentTypeId: Number(form.equipmentTypeId),
                quantity: Number(form.quantity)
            });

            setOpen(false);
            await load();
        } catch (e) {
            alert(e.response?.data?.message || 'Could not save');
        }
    }

    return (
        <PageShell
            title="Purchases"
            description="Record and review asset purchases by base and equipment type."
        >
            <div className="toolbar">
                <button
                    className="primary"
                    onClick={() => setOpen(true)}
                >
                    + Record purchase
                </button>
            </div>

            <Table
                headers={['Date', 'Base', 'Equipment', 'Qty', 'Reference']}
                rows={rows.map(x => [
                    x.purchaseDate,
                    x.base.name,
                    x.equipmentType.name,
                    x.quantity,
                    x.referenceNumber || '—'
                ])}
            />

            {open && (
                <PurchaseModal
                    form={form}
                    setForm={setForm}
                    bases={bases}
                    types={types}
                    onClose={() => setOpen(false)}
                    onSave={save}
                    admin={user.role === 'ADMIN'}
                />
            )}
        </PageShell>
    );
}

function PurchaseModal({ form, setForm, bases, types, onClose, onSave, admin }) { return <Modal title="Record purchase" onClose={onClose}><form className="form-grid" onSubmit={onSave}><Field label="Base"><select disabled={!admin} required value={form.baseId} onChange={e => setForm({ ...form, baseId: e.target.value })}><option value="">Select base</option>{bases.map(b => <option key={b.id} value={b.id}>{b.name}</option>)}</select></Field><Field label="Equipment"><select required value={form.equipmentTypeId} onChange={e => setForm({ ...form, equipmentTypeId: e.target.value })}><option value="">Select equipment</option>{types.map(t => <option key={t.id} value={t.id}>{t.name}</option>)}</select></Field><Field label="Quantity"><input type="number" min="1" required value={form.quantity} onChange={e => setForm({ ...form, quantity: e.target.value })} /></Field><Field label="Purchase date"><input type="date" required value={form.purchaseDate} onChange={e => setForm({ ...form, purchaseDate: e.target.value })} /></Field><Field label="Reference number"><input value={form.referenceNumber} onChange={e => setForm({ ...form, referenceNumber: e.target.value })} /></Field><div className="form-actions"><button type="button" className="secondary" onClick={onClose}>Cancel</button><button className="primary">Save purchase</button></div></form></Modal> }

function Transfers({ user }) {
    const { bases, types } = useMeta();

    const [rows, setRows] = useState([]);
    const [open, setOpen] = useState(false);

    const [form, setForm] = useState({
        fromBaseId: user.role === 'ADMIN' ? '' : String(user.baseId || ''),
        toBaseId: '',
        equipmentTypeId: '',
        quantity: 1,
        transferDate: today,
        remarks: ''
    });

    const load = async () => {
        const r = await api.get('/transfers');
        setRows(r.data);
    };

    useEffect(() => {
        load();
    }, []);

    async function save(e) {
        e.preventDefault();

        try {
            await api.post('/transfers', {
                ...form,
                fromBaseId: Number(form.fromBaseId),
                toBaseId: Number(form.toBaseId),
                equipmentTypeId: Number(form.equipmentTypeId),
                quantity: Number(form.quantity)
            });

            setOpen(false);
            await load();
        } catch (e) {
            alert(e.response?.data?.message || 'Could not save');
        }
    }

    return (
        <PageShell
            title="Transfers"
            description="Move assets between bases with a timestamped movement history."
        >
            <div className="toolbar">
                <button
                    className="primary"
                    onClick={() => setOpen(true)}
                >
                    + Create transfer
                </button>
            </div>

            <Table
                headers={['Date', 'From', 'To', 'Equipment', 'Qty', 'Status', 'Remarks']}
                rows={rows.map(x => [
                    x.transferDate,
                    x.fromBase.name,
                    x.toBase.name,
                    x.equipmentType.name,
                    x.quantity,
                    x.status,
                    x.remarks || '—'
                ])}
            />

            {open && (
                <Modal
                    title="Create transfer"
                    onClose={() => setOpen(false)}
                >
                    <form className="form-grid" onSubmit={save}>

                        <Field label="From base">
                            <select
                                required
                                value={form.fromBaseId}
                                onChange={e =>
                                    setForm({
                                        ...form,
                                        fromBaseId: e.target.value
                                    })
                                }
                            >
                                <option value="">Select source</option>

                                {(user.role === 'ADMIN'
                                    ? bases
                                    : bases.filter(b => String(b.id) === String(user.baseId))
                                ).map(b => (
                                    <option key={b.id} value={b.id}>
                                        {b.name}
                                    </option>
                                ))}
                            </select>
                        </Field>

                        <Field label="To base">
                            <select
                                required
                                value={form.toBaseId}
                                onChange={e =>
                                    setForm({
                                        ...form,
                                        toBaseId: e.target.value
                                    })
                                }
                            >
                                <option value="">Select destination</option>

                                {bases.map(b => (
                                    <option key={b.id} value={b.id}>
                                        {b.name}
                                    </option>
                                ))}
                            </select>
                        </Field>

                        <Field label="Equipment">
                            <select
                                required
                                value={form.equipmentTypeId}
                                onChange={e =>
                                    setForm({
                                        ...form,
                                        equipmentTypeId: e.target.value
                                    })
                                }
                            >
                                <option value="">Select equipment</option>

                                {types.map(t => (
                                    <option key={t.id} value={t.id}>
                                        {t.name}
                                    </option>
                                ))}
                            </select>
                        </Field>

                        <Field label="Quantity">
                            <input
                                type="number"
                                min="1"
                                required
                                value={form.quantity}
                                onChange={e =>
                                    setForm({
                                        ...form,
                                        quantity: e.target.value
                                    })
                                }
                            />
                        </Field>

                        <Field label="Transfer date">
                            <input
                                type="date"
                                required
                                value={form.transferDate}
                                onChange={e =>
                                    setForm({
                                        ...form,
                                        transferDate: e.target.value
                                    })
                                }
                            />
                        </Field>

                        <Field label="Remarks">
                            <input
                                value={form.remarks}
                                onChange={e =>
                                    setForm({
                                        ...form,
                                        remarks: e.target.value
                                    })
                                }
                            />
                        </Field>

                        <div className="form-actions">
                            <button
                                type="button"
                                className="secondary"
                                onClick={() => setOpen(false)}
                            >
                                Cancel
                            </button>

                            <button className="primary">
                                Create transfer
                            </button>
                        </div>

                    </form>
                </Modal>
            )}
        </PageShell>
    );
}

function Assignments({ user }) {
    const { bases, types } = useMeta();

    const [rows, setRows] = useState([]);
    const [open, setOpen] = useState(false);

    const [form, setForm] = useState({
        baseId: user.role === 'ADMIN' ? '' : String(user.baseId || ''),
        equipmentTypeId: '',
        personnelName: '',
        quantity: 1,
        assignedDate: today
    });

    const load = async () => {
        const r = await api.get('/assignments');
        setRows(r.data);
    };

    useEffect(() => {
        load();
    }, []);

    async function save(e) {
        e.preventDefault();

        try {
            await api.post('/assignments', {
                ...form,
                baseId: Number(form.baseId),
                equipmentTypeId: Number(form.equipmentTypeId),
                quantity: Number(form.quantity)
            });

            setOpen(false);
            await load();
        } catch (e) {
            alert(e.response?.data?.message || 'Could not save');
        }
    }

    return (
        <PageShell
            title="Assignments"
            description="Assign assets to personnel and track active assignments."
        >
            <div className="toolbar">
                <button
                    className="primary"
                    onClick={() => setOpen(true)}
                >
                    + Assign asset
                </button>
            </div>

            <Table
                headers={['Date', 'Base', 'Equipment', 'Personnel', 'Qty', 'Status']}
                rows={rows.map(x => [
                    x.assignedDate,
                    x.base.name,
                    x.equipmentType.name,
                    x.personnelName,
                    x.quantity,
                    x.status
                ])}
            />

            {open && (
                <Modal
                    title="Assign asset"
                    onClose={() => setOpen(false)}
                >
                    <form className="form-grid" onSubmit={save}>

                        <Field label="Base">
                            <select
                                disabled={user.role !== 'ADMIN'}
                                required
                                value={form.baseId}
                                onChange={e =>
                                    setForm({
                                        ...form,
                                        baseId: e.target.value
                                    })
                                }
                            >
                                <option value="">Select base</option>

                                {bases.map(b => (
                                    <option key={b.id} value={b.id}>
                                        {b.name}
                                    </option>
                                ))}
                            </select>
                        </Field>

                        <Field label="Equipment">
                            <select
                                required
                                value={form.equipmentTypeId}
                                onChange={e =>
                                    setForm({
                                        ...form,
                                        equipmentTypeId: e.target.value
                                    })
                                }
                            >
                                <option value="">Select equipment</option>

                                {types.map(t => (
                                    <option key={t.id} value={t.id}>
                                        {t.name}
                                    </option>
                                ))}
                            </select>
                        </Field>

                        <Field label="Personnel name">
                            <input
                                required
                                value={form.personnelName}
                                onChange={e =>
                                    setForm({
                                        ...form,
                                        personnelName: e.target.value
                                    })
                                }
                            />
                        </Field>

                        <Field label="Quantity">
                            <input
                                type="number"
                                min="1"
                                required
                                value={form.quantity}
                                onChange={e =>
                                    setForm({
                                        ...form,
                                        quantity: e.target.value
                                    })
                                }
                            />
                        </Field>

                        <Field label="Assignment date">
                            <input
                                type="date"
                                required
                                value={form.assignedDate}
                                onChange={e =>
                                    setForm({
                                        ...form,
                                        assignedDate: e.target.value
                                    })
                                }
                            />
                        </Field>

                        <div className="form-actions">
                            <button
                                type="button"
                                className="secondary"
                                onClick={() => setOpen(false)}
                            >
                                Cancel
                            </button>

                            <button className="primary">
                                Save assignment
                            </button>
                        </div>

                    </form>
                </Modal>
            )}
        </PageShell>
    );
}

function Expenditures({ user }) {
    const { bases, types } = useMeta();

    const [rows, setRows] = useState([]);
    const [open, setOpen] = useState(false);

    const [form, setForm] = useState({
        baseId: user.role === 'ADMIN' ? '' : String(user.baseId || ''),
        equipmentTypeId: '',
        quantity: 1,
        expenditureDate: today,
        reason: ''
    });

    const load = async () => {
        const r = await api.get('/expenditures');
        setRows(r.data);
    };

    useEffect(() => {
        load();
    }, []);

    async function save(e) {
        e.preventDefault();

        try {
            await api.post('/expenditures', {
                ...form,
                baseId: Number(form.baseId),
                equipmentTypeId: Number(form.equipmentTypeId),
                quantity: Number(form.quantity)
            });

            setOpen(false);
            await load();
        } catch (e) {
            alert(e.response?.data?.message || 'Could not save');
        }
    }

    return (
        <PageShell
            title="Expenditures"
            description="Record assets consumed, lost, damaged or otherwise expended."
        >
            <div className="toolbar">
                <button
                    className="primary"
                    onClick={() => setOpen(true)}
                >
                    + Record expenditure
                </button>
            </div>

            <Table
                headers={['Date', 'Base', 'Equipment', 'Qty', 'Reason']}
                rows={rows.map(x => [
                    x.expenditureDate,
                    x.base.name,
                    x.equipmentType.name,
                    x.quantity,
                    x.reason || '—'
                ])}
            />

            {open && (
                <Modal
                    title="Record expenditure"
                    onClose={() => setOpen(false)}
                >
                    <form className="form-grid" onSubmit={save}>

                        <Field label="Base">
                            <select
                                disabled={user.role !== 'ADMIN'}
                                required
                                value={form.baseId}
                                onChange={e =>
                                    setForm({
                                        ...form,
                                        baseId: e.target.value
                                    })
                                }
                            >
                                <option value="">Select base</option>

                                {bases.map(b => (
                                    <option key={b.id} value={b.id}>
                                        {b.name}
                                    </option>
                                ))}
                            </select>
                        </Field>

                        <Field label="Equipment">
                            <select
                                required
                                value={form.equipmentTypeId}
                                onChange={e =>
                                    setForm({
                                        ...form,
                                        equipmentTypeId: e.target.value
                                    })
                                }
                            >
                                <option value="">Select equipment</option>

                                {types.map(t => (
                                    <option key={t.id} value={t.id}>
                                        {t.name}
                                    </option>
                                ))}
                            </select>
                        </Field>

                        <Field label="Quantity">
                            <input
                                type="number"
                                min="1"
                                required
                                value={form.quantity}
                                onChange={e =>
                                    setForm({
                                        ...form,
                                        quantity: e.target.value
                                    })
                                }
                            />
                        </Field>

                        <Field label="Date">
                            <input
                                type="date"
                                required
                                value={form.expenditureDate}
                                onChange={e =>
                                    setForm({
                                        ...form,
                                        expenditureDate: e.target.value
                                    })
                                }
                            />
                        </Field>

                        <Field label="Reason">
                            <input
                                value={form.reason}
                                onChange={e =>
                                    setForm({
                                        ...form,
                                        reason: e.target.value
                                    })
                                }
                            />
                        </Field>

                        <div className="form-actions">
                            <button
                                type="button"
                                className="secondary"
                                onClick={() => setOpen(false)}
                            >
                                Cancel
                            </button>

                            <button className="primary">
                                Save expenditure
                            </button>
                        </div>

                    </form>
                </Modal>
            )}
        </PageShell>
    );
}
function Audit() { const [rows, setRows] = useState([]); useEffect(() => { api.get('/audit-logs').then(r => setRows(r.data)) }, []); return <PageShell title="Audit Logs" description="Recent state-changing transactions for accountability."><Table headers={['Timestamp', 'User', 'Action', 'Entity', 'Description']} rows={rows.map(x => [new Date(x.timestamp).toLocaleString(), x.user?.username || '—', x.action, x.entityType, x.description])} /></PageShell> }
function Field({ label, children }) { return <label className="field"><span>{label}</span>{children}</label> }
export default App

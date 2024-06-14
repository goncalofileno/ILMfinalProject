import "./Modals.css";

export default function AddResourceModal({ isModalActive, setIsModalActive }) {
  return (
    <>
      {isModalActive && (
        <div>
          <div
            className="modal-background"
            onClick={() => setIsModalActive(false)}
          ></div>
          <form className="ilm-modal">
            fasdfadfasdfadfdasfdafdasf fasdfadfasdfadfdasfdafdasfdfadffd
            fdasfdas
          </form>
        </div>
      )}
    </>
  );
}

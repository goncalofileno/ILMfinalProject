import { Pagination } from "react-bootstrap";

export default function TablePagination({
  totalPages,
  currentPage,
  setCurrentPage,
  setNavigateTableTrigger,
}) {
  const renderPaginationItems = () => {
    const items = [];
    const maxPagesToShow = 5;
    if (totalPages <= maxPagesToShow) {
      for (let number = 1; number <= totalPages; number++) {
        items.push(
          <Pagination.Item
            key={number}
            active={number === currentPage}
            onClick={() => {
              setCurrentPage(number);
              setNavigateTableTrigger((prev) => !prev);
            }}
          >
            {number}
          </Pagination.Item>
        );
      }
    } else {
      let startPage, endPage;
      if (currentPage <= 3) {
        startPage = 1;
        endPage = maxPagesToShow - 1;
      } else if (currentPage + 1 >= totalPages) {
        startPage = totalPages - (maxPagesToShow - 2);
        endPage = totalPages - 1;
      } else {
        startPage = currentPage - 2;
        endPage = currentPage + 1;
      }

      for (let number = startPage; number <= endPage; number++) {
        items.push(
          <Pagination.Item
            key={number}
            active={number === currentPage}
            onClick={() => {
              setCurrentPage(number);
              setNavigateTableTrigger((prev) => !prev);
            }}
          >
            {number}
          </Pagination.Item>
        );
      }

      items.push(<Pagination.Ellipsis key="ellipsis" disabled />);

      items.push(
        <Pagination.Item
          key={totalPages}
          active={totalPages === currentPage}
          onClick={() => {
            setCurrentPage(totalPages);
            setNavigateTableTrigger((prev) => !prev);
          }}
        >
          {totalPages}
        </Pagination.Item>
      );
    }

    return items;
  };

  return (
    <div className="pagination-container">
      <Pagination className="pagination">
        <Pagination.First
          onClick={() => {
            setCurrentPage(1);
            setNavigateTableTrigger((prev) => !prev);
          }}
          disabled={currentPage === 1}
        />
        <Pagination.Prev
          onClick={() => {
            setCurrentPage((prev) => Math.max(prev - 1, 1));
            setNavigateTableTrigger((prev) => !prev);
          }}
          disabled={currentPage === 1}
        />
        {renderPaginationItems()}
        <Pagination.Next
          onClick={() => {
            setCurrentPage((prev) => Math.min(prev + 1, totalPages));
            setNavigateTableTrigger((prev) => !prev);
          }}
          disabled={currentPage === totalPages}
        />
        <Pagination.Last
          onClick={() => {
            setCurrentPage(totalPages);
            setNavigateTableTrigger((prev) => !prev);
          }}
          disabled={currentPage === totalPages}
        />
      </Pagination>
    </div>
  );
}
